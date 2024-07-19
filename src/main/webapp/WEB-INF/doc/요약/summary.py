import tool
import cx_Oracle  # Oracle
import json
import os
import requests

from flask import Flask, request, render_template
from flask_cors import CORS
from sqlalchemy import create_engine  # Pandas -> Oracle

app = Flask(__name__) # __name__ == '__main__'
CORS(app)

@app.get('/qcontents/summary') # GET, http://localhost:5000/summary
def summary_form():
  return render_template('summary.html') # /templates/summary.html

@app.post('/qcontents/summary') # POST, http://localhost:5000/summary
def summary_proc():
  # print("POST 요청 발생함.")
  data = request.json
  article = data['article']
  article = tool.remove_empty_lines(article) # 빈 라인 삭제
  print('-> article:', article)
  
  prompt = f'다음 문서를 요약해줘.\n\n{article}'
  print('-> prompt: ' + prompt)
  
  acc_no = data['acc_no'] # <form> 태그의 'acc_no' input 태그의 값
  print('-> acc_no:', acc_no)
  
  format = '''
    {
      "res": "요약된 문장"
    }
  '''
  
  # response = tool.answer('너는 요약 시스템이야', prompt, format)
  # response = tool.answer('너는 요약 시스템이야', prompt, format, 'gpt-4-turbo')
  response = tool.answer('너는 요약 시스템이야', prompt, format, 'gpt-4o') # 가장 우수한 성능
  print(response)  # {'res': 'Hello.'}

  # response를 JSON 문자열로 변환
  # response_json = json.dumps(response, ensure_ascii=False)
  # print(response_json)
  
  # response를 단순 문자열로 저장
  response_text = response['res']
  
  # Oracle insert
  # -------------------------------------------------------------------------
  # Oracle Connection 연결, kd 계정으로 XE 사용.
  conn = cx_Oracle.connect('team7/69017000@43.202.220.6:1521/XE')
  cursor = conn.cursor()
  
  sql = '''
  INSERT INTO summary(summary_no, acc_no, article, response, sdate)
  VALUES(summary_seq.nextval, :acc_no, :article, :response, sysdate)
  ''' 

  result = cursor.execute(sql, (acc_no, article, response_text))
  print('result:', result) # None: 정상 처리, Exception: 에러
  
  conn.commit()
  conn.close()
  # -------------------------------------------------------------------------
  
  return response

app.run(host="0.0.0.0", port=5001, debug=True) # 0.0.0.0: 어디서나 접속, debug=True: 소스 변경시 자동 재시작

'''
activate ai
python summary.py
'''
