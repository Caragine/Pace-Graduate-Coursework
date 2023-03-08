"""
04-19-2021
CS 632P Topics: Python Programming Spring 2021
Prof. Sarbanes
Project #2
Adam Caragine
"""

import base64
import io
import dash
import dash_table
from dash.dependencies import Input, Output
import dash_core_components as dcc
import dash_html_components as html
import plotly.express as px
import pandas as pd
import logging

logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s %(name)s %(levelname)-8s %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S',
                    filename='./project2log.log',
                    filemode='a')
# get logger
logger = logging.getLogger()

app = dash.Dash(__name__)

app.layout = html.Div([

html.H2("Begin by uploading a file!"),

dcc.Upload(
        id='upload-data',
        children=html.Div([
        'Drag and Drop or ',
        html.A('Select Files')
        ]),
        style={
        'width': '100%',
        'height': '60px',
        'lineHeight': '60px',
        'borderWidth': '1px',
        'borderStyle': 'dashed',
        'borderRadius': '5px',
        'textAlign': 'center',
        'margin': '10px'
         },
        # Allow multiple files to be uploaded
        multiple=True
),

html.Br(),

html.Div([

html.H2("Select Stock(s) to Analyze from Dropdown Menu"),

dcc.Dropdown(id="stock",
             # options=[{"label": "AAPL", "value": "AAPL"},
             #            {"label": "IBM", "value": "IBM"},
             #            {"label": "INTC", "value": "INTC"},
             #            {"label": "FB", "value": "FB"},
             #            {"label": "^DJI", "value": "^DJI"},
             #            {"label": "TSLA", "value": "TSLA"},
             #            {"label": "GME", "value": "GME"}],
             multi=True,
             #value='AAPL',
            placeholder="Select a Stock"),

dcc.Dropdown(id="feature",
             options=[
                 {"label": "Adj Close", "value": "Adj Close"},
                 {"label": "Volume", "value": "Volume"}],
             multi=False,
             placeholder="Select a Feature to Plot"),
             #value="Adj Close"),
html.Br(),
html.Hr(),
html.H1("Interactive Line Plot"),

html.Div("Select a stock from above to load graph and data table..."),

dcc.Graph(id="stock_graph"),
html.Hr(),
html.H1("Data Table"),
html.Div("Select Columns to Hide from Table"),

dcc.Checklist(id="ignore",
    options=[
        {'label': 'Date', 'value': 'Date'},
        {'label': 'Volume', 'value': 'Volume'},
        {'label': 'Adj Close', 'value': 'Adj Close'},
        {'label': 'Stock', 'value': 'Stock'},
        {'label': 'Exchange', 'value': 'Exchange'}]),

html.Br(),

dash_table.DataTable(id="datatable", page_size=20)

])])


def parse_contents(contents, filename):
    content_type, content_string = contents.split(',')

    decoded = base64.b64decode(content_string)
    try:
        if 'csv' in filename:
        # Assume that the user uploaded a CSV file
            data = pd.read_csv(
                io.StringIO(decoded.decode('utf-8')))
            UniqueStocks = data.Stock.unique()  # returns pandas numpy array of unique values for column name
            DataFrameDict = {stock: pd.DataFrame for stock in UniqueStocks}
            for key in DataFrameDict.keys():
                DataFrameDict[key] = data[:][data.Stock == key]
        elif 'xls' in filename:
        # Assume that the user uploaded an excel file
            data = pd.read_excel(io.BytesIO(decoded))
            UniqueStocks = data.Stock.unique()  # returns pandas numpy array of unique values for column name
            DataFrameDict = {stock: pd.DataFrame for stock in UniqueStocks}
            logger.debug(DataFrameDict)
            for key in DataFrameDict.keys():
                DataFrameDict[key] = data[:][data.Stock == key]
    except Exception as e:
        print(e)
        return html.Div([
            'There was an error processing this file.'
        ])

    return DataFrameDict

@app.callback(Output('stock_graph', 'figure'),
[Input('upload-data', 'contents'),
 Input('upload-data', 'filename'),
 Input('stock', 'value'),
 Input('feature', 'value')])
def update_graph(contents, filename, stock, feature):
    if contents:
        contents = contents[0]
        filename = filename[0]
        DataFrameDict = parse_contents(contents, filename)

        if isinstance(stock, list):
            df = pd.DataFrame()
            for stock in stock:
                df = df.append(DataFrameDict[stock])
            fig = px.line(df, x="Date", y=feature, color="Stock")

        else:
            #df = DataFrameDict['AAPL']
            df = pd.DataFrame()
            fig = px.line(df, x="Date", y=feature, color="Stock")

    return fig

@app.callback([Output(component_id='datatable', component_property='columns'),
    Output(component_id='datatable', component_property='data')],
[Input('upload-data', 'contents'),
Input('upload-data', 'filename'),
Input('stock', 'value'),
Input('ignore', 'value')])

def update_table(contents, filename, stock, ignore):
    contents = contents[0]
    filename = filename[0]
    DataFrameDict = parse_contents(contents, filename)
    ignorelist = ignore

    if isinstance(stock, list):
        df = pd.DataFrame()
        for stock in stock:
            df = df.append(DataFrameDict[stock])

    else:
        #df = DataFrameDict['AAPL']
        df = pd.DataFrame()

    if ignore:
        for ignore in ignorelist:
            df = df.drop(ignore, 'columns')

    columns = [{"name": i, "id": i} for i in df.columns]
    data = df.to_dict('records')

    return columns, data

@app.callback(Output(component_id='stock', component_property='options'),
[Input('upload-data', 'contents'),
Input('upload-data', 'filename')])

def update_table(contents, filename):
    contents = contents[0]
    filename = filename[0]
    DataFrameDict = parse_contents(contents, filename)

    return [{'label': key, 'value': key} for key in DataFrameDict]

if __name__ == '__main__':
    app.run_server()
    #app.run_server(debug=True)