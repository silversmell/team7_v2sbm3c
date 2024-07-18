# desk.py
import os
import random
import time

from flask import Flask, request, render_template
from flask_cors import CORS
import cx_Oracle  # Oracle

import tool

from openai import OpenAI

client = OpenAI(
  api_key=os.getenv('OPENAI_API_KEY')
)

app = Flask(__name__)  # __name__ == '__main__'
CORS(app)

# http://localhost:5000/desk?acc_no=2
@app.get('/recommend/desk')
def desk_form():
    acc_no = request.args.get('acc_no')
    print("---> acc_no: " + acc_no)
    
    imgs_choice = [i for i in range(1, 51, 1)] # 1 ~ 50
    print(imgs_choice)
    
    filenames = random.sample(imgs_choice, k=50)
    print(filenames)
    
    return render_template('desk.html', filenames=filenames, acc_no=acc_no) # html template으로 데이터 전달

# http://localhost:5000/desk
@app.post('/recommend/desk')
def desk_proc():
    time.sleep(3)
    data = request.json
    desk_filenames = data['desk']
    acc_no= data['acc_no']
    
    print(acc_no)
    
    desk_filenames= desk_filenames.split(',')
    print('-> desk_filenames:', desk_filenames)
    
    # return desk_filenames
    
    # 파일 이름 추출
    desk = []
    for filename in desk_filenames:
        number = filename.split('.')[0]
        print(number)
        desk.append(number)
        
    print('-> desk:', desk)
    print('-> acc_no:', acc_no)
    
    # return desk
    
    #desk 배열의 요소를 정수로 변경하여 list로 변경
    desk = list(map(int, desk)) # map: 배열의 요소에 함수를 적용하는 기능을 함.
    print('-> desk:', desk)
    
    # return desk
    
    items = []
    for index in range(len(desk)): # 8
        item = f'{desk[index]}.jpg'
        items.append(item)

    items_join = ','.join(items)
    
    print('->items_join:', items_join)
    
    prompt='''
    사용자를 3가지 그룹으로 분류하는 중이야. 가장 선호도가 높은 그룹 3가지를 추천하고 아래의 기준을 적용하여 분류해줘.

    [분류 기준]
    brown: 1.jpg, 2.jpg, 3.jpg, 4.jpg, 5.jpg
    cozy: 6.jpg, 7.jpg, 8.jpg, 9.jpg, 10.jpg
    ivory: 11.jpg, 12.jpg, 13.jpg, 14.jpg, 15.jpg
    kitsch: 16.jpg, 17.jpg, 18.jpg, 19.jpg, 20.jpg
    modern: 21.jpg, 22.jpg, 23.jpg, 24.jpg, 25.jpg
    nature: 26.jpg, 27.jpg, 28.jpg, 29.jpg, 30.jpg
    pink: 31.jpg, 32.jpg, 33.jpg, 34.jpg, 35.jpg
    study: 36.jpg, 37.jpg, 38.jpg, 39.jpg, 40.jpg
    work: 41.jpg, 42.jpg, 43.jpg, 44.jpg, 45.jpg
    y2k: 46.jpg, 47.jpg, 48.jpg, 49.jpg, 50.jpg

    [사용자가 선택한 이미지]
    ''' + items_join

    print('-> prompt:', prompt)
    
    format = '{"res": "최우선 추천/중간 추천/마지막 추천"}'

    response = tool.answer(role='너는 방을 꾸며주는 회사 직원이야', prompt=prompt, output='json', format=format, llm='gpt-4o')
    print('-> response:', response)
    
    labels = ['ivory', 'brown', 'pink', 'study', 'nature', 'modern', 'kitsch', 'y2k', 'cozy', 'work']
    
    recommends = response['res'].split('/')
    print(recommends)
    
    conn = cx_Oracle.connect('team7/69017000@43.202.220.6:1521/XE')
    cursor = conn.cursor()

    print('-> acc_no: ' + acc_no)                 
    sql1 = 'DELETE FROM RECOMMEND WHERE acc_no=:acc_no'
    # cursor.execute(sql1, {'acc_no': acc_no})
    cursor.execute(sql1, (acc_no, ))
                    
    # DBMS
    for seq, recom in enumerate(recommends):
        for i, item in enumerate(labels):
            if recom.strip() == item:
                print(f'{seq+1} {i+1} {item}')

                # SQL
                sql2 = '''
                INSERT INTO RECOMMEND(recom_no, acc_no, tag_no, recom_seq, recom_date)
                VALUES(RECOMMEND_SEQ.nextval, :acc_no, :tag_no, :recom_seq, sysdate)
                ''' 
                
                cursor.execute(sql2, (acc_no, i+1, seq+1))
                  
    conn.commit()
    conn.close()
    
    return response  # json 객체 전달

app.run(host="0.0.0.0", port=5000, debug=True)  # 0.0.0.0: 모든 Host 에서 접속 가능, debug=True: 소스 변경시 자동 restart

'''
activate ai
python desk.py
'''
