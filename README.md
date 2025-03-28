# NCS (NoCopyrightSounds)

### ❗ Note
```
이 프로젝트는 Kotlin Multiplatform 버전으로 마이그레이션되어 프라이빗 레포지토리로 이전되었습니다. 접근이 필요하신 경우 문의해 주세요.

This project has been migrated to a Kotlin Multiplatform version and moved to a private repository. If you need access, please contact me.
```

### 📝 Summary
[NCS (NoCopyrightSounds)](https://ncs.io/) 음악 플레이어

### 📱 Application
[Play Store](https://play.google.com/store/apps/details?id=com.ccc.ncs)

### ✅ Features
- [x] 음악 검색 및 Genre, Mood로 필터링
- [x] 음악 재생
- [x] 가사 보기
- [x] 플레이리스트 관리 및 재생
- [x] 음악 순서 변경
- [x] 오프라인 재생 지원(캐시, 다운로드)

<p>
<img width="32.5%" src="resource/nmp_player_small.gif">
<img width="32.5%" src="resource/nmp_playing_small.gif">
<img width="32.5%" src="resource/nmp_detail_small.gif">
<img width="32.5%" src="resource/nmp_delete_small.gif">
<img width="32.5%" src="resource/nmp_search_small.gif">
<img width="32.5%" src="resource/nmp_lyrics_small.gif">
</p>


### 📝 Todos
<details>
<summary>접기/펼치기</summary>

- [x] 음악 상세 페이지
- [x] 아티스트 상세 페이지
- [ ] Light, Dark 모드
- [x] 오픈소스 표기 페이지
- [ ] Fandom wiki path 검토
- [ ] 백엔드 추가
    - [ ] 검색 및 필터링 변경
    - [ ] 추천 기능
- [x] 플레이리스트 제목 글자수 유효성
- [ ] 플레이리스트 선택시 새로운 플레이리스트 추가
- [x] 음악 클릭하여 재생
- [ ] 음악 디테일 페이지 - 가사 없을 때 문구
- [ ] 음악, 아티스트 리스트 페이지 - 결과 없을 때 문구
- [x] 음악 선택시 선택한 개수 표시
- [x] 페이지 전환 애니매이션
- [x] 디테일 페이지 히스토리 처리
- [ ] 플레이 화면에서 음악, 아티스트 디테일 이동시 뒤로 왔을 때 작아지는 현상 수정
- [x] 다운로드 기능
  - [x] 사용자가 직접 파일 다운로드
  - [x] 재생할 음악, 재생중인 음악 캐싱
  - [x] 캐싱 기능 on / off
  - [x] 캐싱 max size 지정
  - [x] 사용자가 다운로드 한 음악, 캐싱된 음악 삭제 페이지 (필터링으로 구분)
- [x] 플레이어 음악 리스트에서 음악 클릭시 클릭한 음악 재생
- [x] 플레이 리스트 상세 페이지에서 음악 클릭시 재생
</details>



### 🛠️ Tech
`Compose`, `Media3`, `Hilt`, `Coroutines`, `Flow`, `Retrofit`, `Room`, `Coil`, `Unit Test`

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
