import './App.css'
import { useEffect } from 'react'

const onNaverLogin = () => {
  window.location.href = "http://localhost:8080/oauth2/authorization/kakao"
}

const getData = () => {
  fetch("http://localhost:8080/my", {
    method: "GET",
    credentials: "include"
  })
      .then((res) => {
        // 👉 여기서 헤더에서 값을 읽음
        const accessToken = res.headers.get("hello");
        alert(accessToken)
          // ✅ 토큰이 있다면 클라이언트를 redirect
          window.location.href = `http://localhost:3000/oauth-success?accessToken=${accessToken}`;
      })
      .catch((error) => {
        alert("에러: " + error);
      });
}

function App() {

  // ✅ 쿼리에서 토큰 꺼내서 alert 하기
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const accessToken = params.get("accessToken");
    if (accessToken) {
      alert("Access Token: " + accessToken);
    }
    window.history.replaceState({}, document.title, "http://localhost:3000/");
  }, []);

  return (
      <>
        <button onClick={onNaverLogin}>NAVER LOGIN</button>
        <button onClick={getData}>GET DATA</button>
      </>
  );
}

export default App
