# HY-FIT: 정밀 수직 위치 기반 종합 피트니스 및 건강 관리 애플리케이션

<p align="center">
<img src="https://github.com/user-attachments/assets/aecf37ee-a1c5-4e32-b86a-9dcd8bad0a9e"  width="130" height="200"/>
</p>

# HyFit - Android Repository
### [2023-1] 한양대학교ERICA 컴퓨터캡스톤디자인
<br/>



## 🖊️ Environment
<img src="https://img.shields.io/badge/intellij-000000?style=for-the-badge&logo=intellijidea&logoColor=white"> <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">  



<br/>

## 📚 Stack
<img src="https://img.shields.io/badge/android-34A853?style=for-the-badge&logo=android&logoColor=white"> <img src="https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"> <img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white"> <img src="https://img.shields.io/badge/glide-18BED4?style=for-the-badge&logo=glide&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/socket.io-010101?style=for-the-badge&logo=socket.io&logoColor=white">

<br/>

## 🤙🏻 Communication
<img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white"> <img src="https://img.shields.io/badge/discord-5865F2?style=for-the-badge&logo=discord&logoColor=white"> <img src="https://img.shields.io/badge/kakaotalk-FFCD00?style=for-the-badge&logo=kakaotalk&logoColor=black"> 

<br/>
<br/>




## 목차 / Table of Contents
1. [📝 개발 배경 및 목표 / Overview](#-개발-배경-및-목표--overview)
2. [💿 사용 기술 및 동작 원리 / Stack & How it works](#-사용-기술-및-동작-원리--stack--how-it-works)
3. [👩🏻‍💻 주요 기능 및 시연 영상 / Features & Screens](#-주요-기능-및-시연-영상--features--screens)
4. [📂 레포지토리 구조 / Repository Structure](#-레포지토리-구조--repository-structure)


<br/>
<br/>
<br/>


## 📝 개발 배경 및 목표 / Overview

<img width="452" alt="git1" src="https://github.com/user-attachments/assets/f4265327-a448-4456-89a4-bd5d9556e2fb">

<br/>
<br/>

## 💿 사용 기술 및 동작 원리 / Stack & How it works
<img width="452" alt="git2" src="https://github.com/user-attachments/assets/432551ec-6e2a-43d9-a7e4-1d361eda46bd">
<br/>
<br/>

## 👩🏻‍💻 주요 기능 및 시연 영상 / Features & Screens

| 함께 운동하기(실외운동)                                                                                          | 
|--------------------------------------------------------------------------------------------------------|
|![함께운동하기실외](https://github.com/user-attachments/assets/7c556063-a1c7-44e1-bba1-18298635629c)|

| 실내 운동                                                                                                  |
|--------------------------------------------------------------------------------------------------------|
|![실내운동](https://github.com/user-attachments/assets/82b60c64-97a9-4b69-9413-5d13e45fd0d1)|

| 회원 리포트                                                                                        |
|-----------------------------------------------------------------------------------------------|
|![회원리포트](https://github.com/user-attachments/assets/fb9a3ed6-c6e5-4169-b160-afe6c7d3a4e5)|

| 목표 설정                                                                                         |
|-----------------------------------------------------------------------------------------------|
|![목표설정](https://github.com/user-attachments/assets/95ef10a8-6110-48b8-8259-21bbea37432e)|

| 커뮤니티                                                                                          |
|-----------------------------------------------------------------------------------------------|
|![커뮤니티](https://github.com/user-attachments/assets/4f224eee-732a-4abf-9ab3-7291f08693fe)|



<br/>
<br/>
<br/>

## 📂 레포지토리 구조 / Repository Structure
```
├── AndroidManifest.xml
├── ic_launcher-playstore.png
└── java
└── com
└── example
└── hyfit_android
├── ApiPathConstants.kt
├── GetResponse.kt
├── Join
│   ├── JoinActivity1.kt
│   ├── JoinActivity2.kt
│   ├── JoinActivity3.kt
│   ├── JoinActivity4.kt
│   ├── JoinActivity5.kt
│   ├── JoinEmailActivity.kt
│   ├── JoinEmailView.kt
│   ├── JoinReq.kt
│   └── JoinView.kt
├── Login
│   ├── FindPasswordActivity1.kt
│   ├── FindPasswordActivity2.kt
│   ├── FindPasswordActivity3.kt
│   ├── FindPasswordReq.kt
│   ├── FindPasswordView.kt
│   ├── LoginActivity.kt
│   ├── LoginReq.kt
│   ├── LoginView.kt
│   ├── LogoutActivity.kt
│   └── LogoutView.kt
├── MainActivity.kt
├── MainFragment.kt
├── MapsActivity.kt
├── ReportFragment.kt
├── ReportResponse.kt
├── ReportRetrofitClass.kt
├── ReportRetrofitInterface.kt
├── ReportRetrofitService.kt
├── ReportView.kt
├── SetFragment.kt
├── SplashActivity.kt
├── UserInfo
│   ├── EditAccountInfoActivity.kt
│   ├── EditPasswordActivity1.kt
│   ├── GetUserView.kt
│   ├── PasswordCheckView.kt
│   ├── PasswordUpdateView.kt
│   ├── UpdateUserReq.kt
│   ├── UpdatepassReq.kt
│   ├── UserGetByEmailView.kt
│   ├── ValidExpiredActivity.kt
│   └── ValidView.kt
├── UserResponse.kt
├── UserRetrofitClass.kt
├── UserRetrofitInterface.kt
├── UserRetrofitService.kt
├── community
│   ├── AddFollowView.kt
│   ├── CommentRVAdapter.kt
│   ├── CommunityFragment.kt
│   ├── CommunityRVAdapter.kt
│   ├── DefaultCommunityRes.kt
│   ├── DeleteCommentView.kt
│   ├── DeletePostView.kt
│   ├── FollowListRes.kt
│   ├── FollowResponse.kt
│   ├── FollowRetrofitInterface.kt
│   ├── FollowService.kt
│   ├── GetAllPostsOfAllUsersView.kt
│   ├── GetAllPostsOfFollowingUsersView.kt
│   ├── GetAllPostsOfUserRes.kt
│   ├── GetAllPostsOfUserView.kt
│   ├── GetCommentListRes.kt
│   ├── GetCommentListView.kt
│   ├── GetCommunityProfileView.kt
│   ├── GetFollowerView.kt
│   ├── GetFollowingView.kt
│   ├── GetOnePostRes.kt
│   ├── GetOnePostView.kt
│   ├── LikePostResponse.kt
│   ├── LikePostView.kt
│   ├── ModifyPostView.kt
│   ├── MyPageFragment.kt
│   ├── MyPageRVAdapter.kt
│   ├── OnChangeFollowListener.kt
│   ├── OnMyPostClickListener.kt
│   ├── OnPostChangeListener.kt
│   ├── OnPostClickListener.kt
│   ├── OnPostSaveListener.kt
│   ├── PostFragment.kt
│   ├── PostLikeRes.kt
│   ├── PostPageRes.kt
│   ├── PostProfileRes.kt
│   ├── PostRes.kt
│   ├── PostRetrofitClass.kt
│   ├── PostRetrofitInterface.kt
│   ├── PostService.kt
│   ├── SaveCommentReq.kt
│   ├── SaveCommentRes.kt
│   ├── SaveCommentView.kt
│   ├── SavePostReq.kt
│   ├── SavePostView.kt
│   ├── UnfollowView.kt
│   ├── UnlikePostView.kt
│   └── UpdateProfileImageView.kt
├── exercise
│   ├── ClimbingActivity.kt
│   ├── ClimbingResultActivity.kt
│   ├── ClimbingResultFragment1.kt
│   ├── ClimbingResultFragment2.kt
│   ├── DeleteExerciseView.kt
│   ├── EndExerciseView.kt
│   ├── ExerciseActivity.kt
│   ├── ExerciseDeleteRes.kt
│   ├── ExerciseEndReq.kt
│   ├── ExerciseListRes.kt
│   ├── ExerciseRes.kt
│   ├── ExerciseResultActivity.kt
│   ├── ExerciseRetrofitInterface.kt
│   ├── ExerciseService.kt
│   ├── ExerciseStartReq.kt
│   ├── ExerciseStartView.kt
│   ├── GetAllExerciseView.kt
│   ├── GetExerciseByGoalView.kt
│   ├── GetExerciseView.kt
│   ├── StairActivity.kt
│   ├── StairResultActivity.kt
│   ├── ViewPagerAdapter.kt
│   └── exerciseWith
│       ├── DeleteExerciseRes.kt
│       ├── DeleteExerciseWithView.kt
│       ├── ExerciseWithInterface.kt
│       ├── ExerciseWithReq.kt
│       ├── ExerciseWithReq1.kt
│       ├── ExerciseWithRes.kt
│       ├── ExerciseWithService.kt
│       ├── FollowingClickListener.kt
│       ├── FollowingSelectRVAdaptor.kt
│       ├── GetExerciseWithView.kt
│       ├── GoalSelectFragment3.kt
│       ├── ReadyExerciseRes.kt
│       ├── ReadyExerciseWithView.kt
│       ├── RequestExerciseView.kt
│       ├── RequestFragment.kt
│       ├── SelectFollowingFragment.kt
│       ├── SocketConfig.kt
│       ├── StartExerciseView.kt
│       ├── TypeSelectWithFragment.kt
│       ├── climbing
│       │   ├── ClimbingWithActivity.kt
│       │   └── ClimbingWithResultActivity.kt
│       └── exercise
│           └── ExerciseWithActivity.kt
├── goal
│   ├── DeleteGoalRes.kt
│   ├── DeleteGoalView.kt
│   ├── GetBuildingView.kt
│   ├── GetDoneGoalView.kt
│   ├── GetGoalImageRes.kt
│   ├── GetGoalImageView.kt
│   ├── GetGoalRecView.kt
│   ├── GetGoalReq.kt
│   ├── GetGoalRes.kt
│   ├── GetGoalView.kt
│   ├── GetMountainView.kt
│   ├── GetPlacePageRes.kt
│   ├── GetPlacePageView.kt
│   ├── GetPlaceRes.kt
│   ├── GetPlaceView.kt
│   ├── GoalDetailRVAdapter.kt
│   ├── GoalFragment.kt
│   ├── GoalModalDelete.kt
│   ├── GoalModalFragment.kt
│   ├── GoalModalFragment2.kt
│   ├── GoalModalFragment3.kt
│   ├── GoalPlaceDetailRVAdapter.kt
│   ├── GoalRetrofitInterface.kt
│   ├── GoalSearchRVAdapter.kt
│   ├── GoalService.kt
│   ├── ModifyGoalView.kt
│   ├── OnGoalChangeListener.kt
│   ├── OnGoalClickListener.kt
│   ├── OnGoalSaveListener.kt
│   ├── OnItemClickListener.kt
│   ├── PlaceReq.kt
│   ├── PlaceService.kt
│   ├── SaveGoalReq.kt
│   ├── SaveGoalRes.kt
│   ├── SaveGoalView.kt
│   └── info
│       ├── ClimbingInfoActivity.kt
│       ├── ClimbingInfoFragment1.kt
│       ├── ClimbingInfoFragment2.kt
│       ├── ExerciseDataActivity.kt
│       ├── ExerciseDataRVAdaptor.kt
│       ├── ExerciseInfoMapActivity.kt
│       ├── GoalResultActivity.kt
│       ├── OnExerciseClickListener.kt
│       ├── StairInfoActivity.kt
│       └── ViewPagerAdapter2.kt
├── home
│   ├── GoalRecFragment.kt
│   ├── GoalSelectFragment.kt
│   ├── GoalSelectFragment2.kt
│   ├── GoalSelectRVAdaptor.kt
│   ├── ImageAdapter.kt
│   ├── MainFragment.kt
│   ├── MapsFragment.kt
│   ├── OnGoalClickListener.kt
│   └── TypeSelectFragment.kt
├── location
│   ├── GetAllExerciseListView.kt
│   ├── GetAllRedisExerciseView.kt
│   ├── GetRedisExerciseView.kt
│   ├── LocationAllRedisRes.kt
│   ├── LocationAltRedisReq.kt
│   ├── LocationExerciseRes.kt
│   ├── LocationExerciseSaveReq.kt
│   ├── LocationRedisExerciseRes.kt
│   ├── LocationRedisReq.kt
│   ├── LocationRedisRes.kt
│   ├── LocationRetrofitInterface.kt
│   ├── LocationService.kt
│   ├── SaveAltRedisLocView.kt
│   ├── SaveExerciseLocView.kt
│   └── SaveExerciseRedisLocView.kt
├── pinnacle
│   └── PinnacleActivity.kt
└── report
├── ActivityDashboardActivity.kt
├── RunningInfoActivity.kt
├── UserbodyActivity.kt
├── UserbodyReq.kt
└── UserbodyView.kt


```


