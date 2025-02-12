import './App.css'

const onNaverLogin = () => {
  window.location.href = "http://localhost:8080/oauth2/authorization/kakao"
}

const getData=()=>{
  fetch("http://localhost:8080/my",{
    method:"GET",
    credentials : "include"
  }).then((res)=>res.text())
  .then((data)=>{
    alert(data)
  })
  .catch((error)=>{
    alert(error)
  })
}

function App(){
  return(
    <>
    <button onClick={onNaverLogin}>NAVER LOGIN</button>
    <button onClick={getData}>GET DATA</button>
    </>
  )
}

export default App
