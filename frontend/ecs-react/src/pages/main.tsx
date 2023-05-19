import { useNavigate } from "react-router-dom"
import { parseJwt } from "../services/jwtDecode"
import button from "../assets/image/kakao_button.png"
import Title from "../components/main/title"
import style from "../styles/main/main.module.css"
import { useEffect, useState } from "react"
import { Cookies } from "react-cookie"
import { useRecoilState } from "recoil"
import HelpModal from "../components/modal/helpModal"
import helpIcon from "../assets/help-bubble.png"
import { helpModal } from "../recoil/atoms/modalState"
export default function Main() {
  const navigate = useNavigate()
  const cookies = new Cookies()
  const userNo = sessionStorage.getItem("userNo")

  const log = sessionStorage.getItem("log")

  const [modal, setModal] = useRecoilState(helpModal)

  const closeModal = () => {
    setModal(false)
  }

  useEffect(() => {
    console.log(userNo)
    const token = cookies.get("accessToken")
    if (token !== undefined) {
      const obj = parseJwt(token)
      sessionStorage.setItem("userNo", obj.no)
      sessionStorage.setItem("userName", obj.name)
    }
  }, [])
  return (
    <div className={style.continer}>
      <div className={style.wrapper}>
        <Title></Title>
        {userNo !== null ? (
          <div className={style.box}>
            <div className={style.btnBox}>
              <button
                className={style.btn}
                onClick={() => {
                  navigate("/setting")
                }}
              >
                시작하기
              </button>
            </div>
          </div>
        ) : (
          <div className={style.box}>
            <div
              style={{ textAlign: "center" }}
              onClick={() => {
                window.location.href =
                  "https://k8d204.p.ssafy.io/api/oauth2/authorization/kakao"
              }}
            >
              <button style={{ background: "none" }}>
                <img src={button} alt='' style={{ cursor: "pointer" }}></img>
              </button>
            </div>
          </div>
        )}
      </div>
      <div
        className={style.helpIcon}
        onClick={() => {
          setModal(true)
          setTimeout(closeModal, 5000)
        }}
      >
        <img src={helpIcon} alt='' width={100}></img>
      </div>
      {modal ? <HelpModal /> : null}
    </div>
  )
}
