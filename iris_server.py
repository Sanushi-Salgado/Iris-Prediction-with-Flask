#!/usr/bin/env python3

from flask import Flask, jsonify, request
import json
import pickle
import numpy as np

app = Flask(__name__)


# Load the model
model = pickle.load(open('models/SVMModel.pckl', 'rb'))


@app.route('/')
def hello_world():
    return 'Hello World!'


@app.route('/add/', methods = ['POST'])
def add_numbers():
    if request.method == 'POST':
        decoded_data = request.data.decode('utf-8')
        params = json.loads(decoded_data)
        return jsonify( {'sum': params['x'] + params['y'] }
                      )

@app.route('/postjson', methods = ['POST'])
def postJsonHandler():
    print (request.is_json)
    content = request.get_json()
    print (content)
    return 'JSON posted'

        
@app.route('/predict', methods = ['POST'])
def predict_numbers():
    if request.method == 'POST':
        # Get the keys        
        sepLen = request.form.keys()[0]
        sepWid = request.form.keys()[1]
        petLen = request.form.keys()[2]
        petWid = request.form.keys()[3]
        
        # Get the values of keys 
        post_data = request.form
        sepLen = post_data['sepLen']
        sepWid = post_data['sepWid']
        petLen = post_data['petLen']
        petWid = post_data['petWid']
         
        
#         return jsonify( { 
#             'sepalLen': int(sepLen),
#             'sepWid': int(sepWid),
#             'petLen': int(petLen),
#             'petWid': int(petWid)
#             } )  
        
        
        new_data = np.array([[int(sepLen), int(sepWid), int(petLen), int(petWid)]]).reshape(1,4)
        class_prediced = int(model.predict(new_data)[0])
        output = "Predicted Iris Class: " + str(class_prediced)
#         
        return jsonify( { 'location': str(class_prediced) } )                


if __name__ == '__main__':
    app.run(debug=True)
    