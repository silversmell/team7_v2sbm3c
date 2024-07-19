import json
import os
import requests
import random

from datetime import datetime
from PIL import Image

import tool

import cx_Oracle  # Oracle
from sqlalchemy import create_engine  # Pandas -> Oracle

from openai import OpenAI

client = OpenAI(
  api_key=os.getenv('OPENAI_API_KEY')
)

from flask import Flask, request, render_template
from flask_cors import CORS

import tool # tool.py

app = Flask(__name__)  # __name__ == '__main__'
CORS(app)

def is_image_file(file_path):
    """
    파일이 이미지 파일인지 확인합니다.

    지원하는 이미지 파일 형식: jpg, jpeg, png, bmp, gif, tiff
    """
    valid_image_extensions = ['.jpg', '.jpeg', '.png', '.bmp', '.gif', '.tiff']
    return os.path.splitext(file_path)[1].lower() in valid_image_extensions

def resize_image(file_path, scale=0.5):
    file_path_t = rename_file_with_suffix(file_path, '_t') # test_t.jpg 파일명만 조합
    print(f'-> file_path: {file_path}')
    print(f'-> file_path_t: {file_path_t}')
    """
    이미지를 비율에 맞춰 축소합니다.
    """
    if is_image_file(file_path):
        print('-> 이미지입니다.')
        
        with Image.open(file_path) as img:
            original_size = img.size
            new_size = (int(original_size[0] * scale), int(original_size[1] * scale))
            img = img.resize(new_size, Image.Resampling.LANCZOS)
            img.save(file_path_t)
            print(f"이미지가 성공적으로 축소되어 저장되었습니다: {file_path_t}")
            
        return file_path_t
    else:
        print("이미지 파일이 아닙니다.")
        return '이미지 크기 변경에 실패했습니다.'

def rename_file_with_suffix(file_path, suffix):
    """
    파일 이름에 접미사를 추가하여 파일 이름을 변경합니다.
    
    file_path: 원본 파일 경로
    suffix: 추가할 접미사 (예: '_t')
    """
    dir_name, base_name = os.path.split(file_path) # 폴더명과 파일명 분할
    name, ext = os.path.splitext(base_name) # 파일명과 확장자 분할
    new_name = f"{name}{suffix}{ext}" # 새로운 파일명 조합: test_t.jpg
    new_path = os.path.join(dir_name, new_name) # 저장 경로 결합
    
    return new_path

                        
# http://localhost:5000/qcontents/member_img
@app.get('/qcontents/member_img')
def member_img_form():
    return render_template('member_img.html') # /templates/member_img.html

# http://localhost:5000/qcontents/member_img
@app.post('/qcontents/member_img')
def member_img_proc():
    data = request.json  # json 형식으로 읽기
    prompt = data['prompt'] # <form> 태그의 'prompt' input 태그의 값
    print('-> prompt:', prompt)

    acc_no = data['acc_no'] # <form> 태그의 'acc_no' input 태그의 값
    print('-> acc_no:', acc_no)
    
    response = client.images.generate(
        model="dall-e-3",
        prompt=prompt,
        size="1024x1024",
        quality="standard", # standard, hd
        n=1,
    )
    image_url = response.data[0].url
    print(image_url)

    # URL에서 이미지를 가져옴
    response = requests.get(image_url)

    # 현재 시간을 가져옴
    now = datetime.now()

    # '년월일시분초' 형식의 문자열 생성
    date_time_string = now.strftime("%Y%m%d%H%M%S")

    # 1부터 1000까지의 난수 생성
    random_number = random.randint(1, 1000)

    if os.path.exists('./static/member_img') == False:
        os.mkdir('./static/member_img')

    # 고유한 파일명 생성 (년월일시분초_난수.txt 형식)
    file_name = f"{date_time_string}_{random_number}.jpg"
    # spring_boot_file_name = f'/qcontents/storage/{date_time_string}_{random_number}.jpg'
    spring_boot_file_name = f'/openai/member/storage/{date_time_string}_{random_number}.jpg'
    print(f'-> file_name: {file_name}')
    
    # 응답이 성공적인지 확인
    if response.status_code == 200:
        # 이미지 데이터를 파일로 저장
        with open(file_name, "wb") as file:
            file.write(response.content)
        print("이미지가 성공적으로 저장되었습니다.")
        
        # 파일 용량 산출
        file_size = os.path.getsize(file_name)
        print(f'-> file_size: {file_size}')
         
        # 파일 축소 이미지 산출
        file_name_resized = resize_image(file_name, 0.5)
        
        # Oracle insert
        # -------------------------------------------------------------------------
        # Oracle Connection 연결, kd 계정으로 XE 사용.
        conn = cx_Oracle.connect('team7/69017000@43.202.220.6:1521/XE')
        cursor = conn.cursor()
        
        sql = '''
        INSERT INTO dalle(dalle_no, acc_no, prompt,dalle_origin, dalle_thumb, dalle_size, ddate)
        VALUES(dalle_seq.nextval, :acc_no, :prompt,:dalle_origin, :dalle_thumb, :dalle_size, sysdate)
        ''' 

        result = cursor.execute(sql, (acc_no, prompt, file_name, file_name_resized, file_size))
        print('result:', result) # None: 정상 처리, Exception: 에러
        
        conn.commit()
        conn.close()
        # -------------------------------------------------------------------------
        

    else:
        print("이미지를 가져오는데 문제가 발생했습니다.")

    file_name = {"file_name": spring_boot_file_name}

    # return json.loads(file_name) # str -> json(dict)
    return json.dumps(file_name) # json(dict) -> str

app.run(host="0.0.0.0", port=5002, debug=True)  # 0.0.0.0: 모든 Host 에서 접속 가능, debug=True: 소스 변경시 자동 restart

'''
activate ai
python member_img.py
http://localhost:5000/member_img
'''

