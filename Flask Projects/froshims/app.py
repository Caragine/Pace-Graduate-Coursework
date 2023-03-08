from flask import Flask, render_template, request
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)

registrants = {} # empty dictionary

sports = ["Basketball", "Soccer", "Ultimate Frisbee", "Football"]

@app.route("/")
def index():
    return render_template("index.html", sports=sports)

@app.route("/register", methods=["POST"])
def register():
    # Validate submission
    if not request.form.get("name") or request.form.get("sport") not in sports:
        return render_template("failure.html")
    
    # Confirm registration
    name = request.form.get("name") #request.args.get for get requests
    sport = request.form.get("sport")
    #member = [name, sport]
    #members.append(member)
    #print(members)
    return render_template("success.html")
    #return render_template("success.html", members=members)

