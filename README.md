# NCS (NoCopyrightSounds)

### 📝 Summary
[NCS (NoCopyrightSounds)](https://ncs.io/) 전용 플레이어

### ✅ Features
- [x] 음악 검색 및 Genre, Mood로 필터링
- [x] 음악 재생
- [x] 가사 보기
- [x] 플레이리스트 관리 및 재생
- [x] 음악 순서 변경

<p>
<img width="32.5%" src="resource/nmp_player_small.gif">
<img width="32.5%" src="resource/nmp_playing_small.gif">
<img width="32.5%" src="resource/nmp_detail_small.gif">
<img width="32.5%" src="resource/nmp_delete_small.gif">
<img width="32.5%" src="resource/nmp_search_small.gif">
<img width="32.5%" src="resource/nmp_lyrics_small.gif">
</p>

### 📝 Todos
- [x] 음악 상세 페이지
- [x] 아티스트 상세 페이지
- [ ] Light, Dark 모드
- [x] 오픈소스 표기 페이지
- [ ] Fandom wiki path 검토
- [ ] 백엔드 추가시 검색 및 필터링 변경

### 🛠️ Tech
`Compose`, `Media3`, `Hilt`, `Coroutines`, `Flow`, `Retrofit`, `Room`, `Coil`, `Unit Test`

### 📖 How to run
- `local.properties`파일에 아래 항목 추가
- add the below data to `local.properties`
```
WEB_URL="https://ncs.io"
FANDOM_URL="https://nocopyrightsounds.fandom.com"
```


> #### For release mode
1. `app`디렉토리에 keystore 파일 추가
- add keystore file to `app` directory 

2. `root`디렉토리에 `keystore.properties`파일 추가, keystore 정보 입력
- add `keystore.properties` file to `root` directory and write keystore data following below
```
RELEASE_KEY_STORE={KEY_STORE_FILE_NAME}
RELEASE_KEY_PASSWORD={KEY_STORE_KEY_PASSWORD}
RELEASE_KEY_ALIAS={KEY_STORE_KEY_ALIAS}
RELEASE_KEY_ALIAS_PASSWORD={KEY_STORE_ALIAS_PASSWORD}

# example
# RELEASE_KEY_STORE=ncs.keystore
# RELEASE_KEY_PASSWORD=my_password
# RELEASE_KEY_ALIAS=alias0
# RELEASE_KEY_ALIAS_PASSWORD=alias0_password
```




### 📱 Result

<p>
<img width="32.5%" src="resource/ncs_animation.gif">
<img width="32.5%" src="https://github.com/moonggae/ncs/assets/74220198/c3a29934-9b02-4f56-bd2b-fee97d92f4f8">
<img width="32.5%" src="https://github.com/user-attachments/assets/47dc36df-a856-41a1-ad01-b94f039abe72">
<img width="32.5%" src="https://github.com/user-attachments/assets/d2fd6b75-bdae-4cef-a3c4-2ca6c4a92f57">
<img width="32.5%" src="https://github.com/user-attachments/assets/eaa2ba87-2d19-4e69-9d06-84590d4e7efa">
<img width="32.5%" src="https://github.com/moonggae/ncs/assets/74220198/6353d113-4d85-41a1-a82e-9062ecb128f7">
<img width="32.5%" src="https://github.com/moonggae/ncs/assets/74220198/3c4e6522-ac24-475c-990f-d49d37ca7e08">
<img width="32.5%" src="https://github.com/moonggae/ncs/assets/74220198/373eff5c-d740-4365-81c4-f56dbbf61380">
<img width="32.5%" src="https://github.com/moonggae/ncs/assets/74220198/31694a65-cb2c-4959-90b2-c67c6758e3c4">
<img width="32.5%" src="https://github.com/moonggae/ncs/assets/74220198/8e5d1d1b-a643-4e4d-8712-c232ea05c539">
</p>