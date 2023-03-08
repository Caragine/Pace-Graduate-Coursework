from flask import Flask, render_template, request, redirect
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import delete

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///db.sqlite3'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)

class Bets(db.Model):
   id = db.Column(db.Integer, primary_key = True)
   name1 = db.Column(db.String(50))
   betamount = db.Column(db.Integer)
   towin = db.Column(db.Integer)
   name2 = db.Column(db.String(50))
   betinfo = db.Column(db.String(200))

def __init__(self, name1, betamount, towin, name2, betinfo):
   self.name1 = name1
   self.betamount = betamount
   self.towin = towin
   self.name2 = name2
   self.betinfo = betinfo


@app.route("/")
def index():
    return render_template("index.html")

@app.route("/bigboard", methods=["GET", "POST"])
def bigboard():
    # Validate submission
    if request.method == "GET":
        return render_template("bigboard.html", bets = Bets.query.all())

    if not request.form.get("name1") or not request.form.get("betamount") or not request.form.get("towin") or not request.form.get("name2") or not request.form.get("betinfo"):
        return render_template("failure.html")

    
    # Confirm submission
    bet = Bets(name1=request.form["name1"], betamount=request.form["betamount"], towin=request.form["towin"], name2=request.form["name2"], betinfo=request.form["betinfo"])
    db.session.add(bet)
    db.session.commit()
    return render_template("bigboard.html", bets = Bets.query.all())



@app.route("/removebet", methods=["GET", "POST"])
def removebet():
    id = request.form.get("id")
    Bets.query.filter_by(id=id).delete() # DELETES UPON PRESSING REMOVE BET BUTTON
    db.session.commit()
    return redirect("/bigboard")

