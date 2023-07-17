# :pushpin: 알약 판별 어플 

## 🖥️ 프로젝트 소개
휴대폰으로 촬영한 이미지를 이용하여 알약에 대한 정보를 클라이언트에게 제공하는 어플리케이션
<br>

## 🕰️ 개발 기간
2023.02 ~ 2023.06
<br>

## 🔧 주요 기능 
- Python 서버와의 half-duplex socket 통신 
- DB(Mysql)로부터 데이터를 받거나 DB에 존재하는 사용자 데이터를 수정한다.

## 📱 실행 화면 
<img src="https://github.com/SEUNGJAE-97/Senior_project/assets/117517535/d98e2637-4b45-4449-b9ce-a3937430d778" width="250" height="400"> 
<img src="https://github.com/SEUNGJAE-97/Senior_project/assets/117517535/9404e208-51b5-4d39-a158-84d6fbae3ef0" width="250" height="400"> 
<img src="https://github.com/SEUNGJAE-97/Senior_project/assets/117517535/232809e5-8ac7-4b77-a046-be2f1b057d43" width="250" height="400"> 
<img src="https://github.com/SEUNGJAE-97/Senior_project/assets/117517535/7152e206-671f-49fa-b031-291f2bb6d2f4" width="250" height="400"> 
<img src="https://github.com/SEUNGJAE-97/Senior_project/assets/117517535/d0d782de-1522-4513-bd41-9efca6ea20c0" width="250" height="400"> 

- 로그인 버튼을 클릭시 사용자 DB의 데이터를 조회하여 응답(True/False)을 받고, 
  True일때 User_ID와 함께 화면전환을 한다. 
- 휴대폰 내장 카메라 어플로 사진을 촬영하여 ImageView에 Bitmap형식으로 출력한다. 

- 서버로 이미지를 전송하고, 이미지에 해당하는 Medicine_ID 값을 전달 받은뒤에 알약 정보를
  출력하는 화면(Result_activity)으로 이동한다.
  
- Result_activity에는 알약 정보 및 본인이 촬영한 이미지와 Glide 라이브러리를 통해 가져온 이미지를 제공한다. 
  추가로 플로팅 버튼을 통해 어플내에서 내비게이션 기능을 추가하였다.
  
- 사용자 DB에 알약을 담을 수 있는 기능을 구현하였다.

- 사용자가 섭취중인 알약 리스트를 보여주는 List_activity화면에서는 삭제 및 알약정보 조회 기능을 구현하였다.

## 📹 시연 영상

[![Video Label](http://img.youtube.com/vi/DMmX1Lnrn_U/0.jpg)](https://youtu.be/DMmX1Lnrn_U)
