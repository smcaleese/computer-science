from flask import Flask, render_template, request
import csv
import json
app = Flask(__name__)

# Part 1
@app.route('/')
def main():
    return render_template('index.html')

# Part 2
@app.route('/formtest', methods=['GET', 'POST'])
def formtest():
    if request.method == 'POST':
        name = request.form['name']
        return render_template('name.html', name=name)
    else:
        return render_template('form.html')

# Part 3
@app.route('/allegiances')
def allegiances():
    output_dict = {}
    with open('allegiances.csv', 'r') as file:
        reader = csv.DictReader(file)
        for row in reader:
            values = list(row.values())
            name, affiliation, allegiance = values
            new_entry = {'Affiliation': affiliation, 'Allegiance': allegiance}
            output_dict[name] = new_entry

    output_json = json.dumps(output_dict)
    return output_json

# Part 4
@app.route('/allegiancedashboard')
def getjson():
    return render_template('getjson.html')

if __name__ == '__main__':
    app.run()