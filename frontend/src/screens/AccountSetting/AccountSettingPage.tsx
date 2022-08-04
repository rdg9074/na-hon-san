import React, { useState, useRef } from "react";
import "./AccountSettingPage.scss";
import { Link, useNavigate } from "react-router-dom";
import UserDummyIcon from "@images/UserDummy.svg";
import { v4 } from "uuid";
import ImgResizer from "@components/common/ImgUploader/ImgResizer";
import { useAppSelector, useAppDispatch } from "@store/hooks";
import { chkNickNameExist } from "@apis/auth";
import { setAccount } from "@apis/setAccount";
import { getUserInfo } from "@store/ducks/auth/authThunk";
import { UserInfoType } from "@store/ducks/auth/authSlice";
import LoadingSpinner from "@images/LoadingSpinner.svg";

function AccountSettingPage() {
  interface setType extends Omit<UserInfoType, "backgroundImg" | "area"> {
    area?: string;
    backgroundImg?: string;
  }
  const tmpUserInfo = useAppSelector(state => state.auth.userInfo);
  const imgInput = useRef<HTMLInputElement>(null);
  const nickNameInput = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  if (!tmpUserInfo) {
    navigate("/");
    return <div />;
  }
  const userInfo = { ...tmpUserInfo } as setType;
  const [errMsg, setErrMsg] = useState(false);
  const [errText, setErrText] = useState("");
  const [isChk, setIsChk] = useState(false);
  const [tmpNickName, setTmpNickName] = useState(userInfo.nickname);
  const [tmpText, setTmpText] = useState(
    userInfo.profileMsg ? userInfo.profileMsg : null
  );
  const [sendFile, setSendFile] = useState<File | null>(null);
  const [userImg, setUserImg] = useState(
    userInfo.profileImg
      ? `data:image/jpeg;base64,${userInfo.profileImg}`
      : UserDummyIcon
  );
  const [spinner, setSpinner] = useState(false);

  // 프로필 이미지 설정 부분
  const clickInput = () => {
    if (imgInput.current) {
      imgInput.current.click();
    }
  };

  const fileread = () => {
    if (imgInput.current?.files) {
      const file = imgInput.current.files[0];
      if (file) {
        setSendFile(file);
      }
    }
  };

  const receiveFile = (data: string) => {
    if (userInfo) {
      setUserImg(data);
      const incData = data.replace("data:image/jpeg;base64,", "");
      userInfo.profileImg = incData;
    }
  };

  // 상태 메세지 설정 부분
  const handleText = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setTmpText(e.target.value);
  };

  const chkLength = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    const state = e.target.value;
    if (state.length > 100) {
      const txt = state.substring(0, 100);
      e.target.value = txt;
      e.target.focus();
      return;
    }
    const rows = state.split("\n");
    if (rows.length > 5) {
      const txt = rows.splice(0, 5);

      e.target.value = txt.join("\n");

      e.target.focus();
    }
    e.target.style.height = "1px";
    e.target.style.height = `${12 + e.target.scrollHeight}px`;
  };

  // 닉네임 중복 체크
  const nickNameChk = async () => {
    if (nickNameInput.current?.value === "") {
      nickNameInput.current.focus();
      return;
    }

    const res = await chkNickNameExist(nickNameInput.current?.value as string);
    setIsChk(true);

    if ((nickNameInput.current?.value.length as number) > 10) {
      setErrMsg(false);
      setErrText("닉네임은 10자 이하입니다.");
    } else if (res === "SUCCESS" && nickNameInput.current) {
      setErrMsg(true);
      setTmpNickName(nickNameInput.current.value);
    } else {
      setErrMsg(false);
      setErrText("중복된 닉네임입니다.");
    }
  };

  // 버튼 라우팅
  const goBack = () => {
    navigate(-1);
  };
  const goWithdrawal = () => {
    navigate("/account/withdrawal", { replace: true });
  };

  // 알람 토글 부분
  const noti = ["좋아요", "팔로잉", "댓글", "대댓글"];
  const followState = ["팔로우", "팔로잉"];
  const [notiList, setNotiList] = useState([
    userInfo.likeNotice,
    userInfo.followNotice,
    userInfo.commentNotice,
    userInfo.replyNotice,
    userInfo.followerOpen,
    userInfo.followOpen
  ]);

  // 전송 부분
  const changeSet = async () => {
    [
      userInfo.likeNotice,
      userInfo.followNotice,
      userInfo.commentNotice,
      userInfo.replyNotice,
      userInfo.followerOpen,
      userInfo.followOpen
    ] = [
      notiList[0],
      notiList[1],
      notiList[2],
      notiList[3],
      notiList[4],
      notiList[5]
    ];
    if (tmpNickName && tmpText) {
      userInfo.nickname = tmpNickName;
      userInfo.profileMsg = tmpText;
    }
    setSpinner(true);
    delete userInfo.area;
    delete userInfo.backgroundImg;
    const res = await setAccount(userInfo as UserInfoType);

    if (res === "SUCCESS") {
      await dispatch(getUserInfo());
      navigate(`/userfeed/${tmpNickName}`);
    }
  };

  return (
    <div id="accountSetting-page">
      <input type="file" ref={imgInput} accept="image/*" onChange={fileread} />
      <div className="setprofile notoMid fs-36">
        <div className="setprofile__title">프로필 설정</div>
        <div className="img-container flex column">
          <button
            type="button"
            className="user-container flex"
            onClick={clickInput}
          >
            <img
              className="img-container__user"
              src={userImg}
              alt="userImg"
              title="userImg"
            />
            <p className="img-container__user-add fs-48">+</p>
          </button>
          {sendFile ? (
            <ImgResizer
              imgfile={sendFile}
              newImgfile={receiveFile}
              imgW={300}
              imgH={300}
            />
          ) : null}
        </div>
        <div className="text-wrapper flex column">
          <p>{userInfo.nickname}</p>
          <textarea
            className="state notoReg"
            defaultValue={tmpText as string}
            onChange={e => handleText(e)}
            onKeyDown={e => chkLength(e)}
          />
          <p className="text-info fs-12">
            최대 100자 줄바꿈은 5번까지 가능합니다.
          </p>
        </div>
      </div>
      <div className="setaccount">
        <div className="title notoMid fs-36 p-none">계정 설정</div>
        <div className="main">
          <div className="main-account__title flex">
            <p className="fs-24 notoMid">회원정보 수정</p>
            <button onClick={goWithdrawal} type="button" className="fs-12">
              회원탈퇴
            </button>
          </div>
          <div className="main-account flex column">
            <div className="main-account__nickname flex">
              <p className="fs-16 notoReg">닉네임</p>
              <div>
                <input
                  type="text"
                  ref={nickNameInput}
                  defaultValue={userInfo.nickname}
                />
                {isChk ? (
                  <p className="notoReg">
                    {errMsg ? (
                      <span className="success">사용 가능한 닉네임입니다.</span>
                    ) : (
                      <span className="err">{errText}</span>
                    )}
                  </p>
                ) : null}
              </div>
              <button
                className="notoMid fs-12"
                type="button"
                onClick={nickNameChk}
              >
                중복확인
              </button>
            </div>
            <div className="main-account__addinfo flex">
              <p className="fs-16 notoReg">지역·태그</p>
              <Link to="/join/more" className="notoMid fs-12">
                변경하기
              </Link>
              <div> </div>
            </div>
            <div className="main-account__addinfo flex">
              <p className="fs-16 notoReg">비밀번호</p>
              <Link to="/reset/pw" className="notoMid fs-12">
                변경하기
              </Link>
              <div> </div>
            </div>
          </div>
          <div className="main-noti__title fs-24 notoMid">알림 설정</div>
          <div className="main-noti__toggle flex">
            {noti.map((title, idx) => {
              return (
                <div className="main-noti__toggle-item flex" key={v4()}>
                  <p className="notoReg fs-14">{title}</p>
                  <button
                    onClick={() => {
                      setNotiList(cur => {
                        const newArr = [...cur];
                        newArr[idx] = !newArr[idx];
                        return newArr;
                      });
                    }}
                    type="button"
                    className={`main-noti__toggle-btn flex ${
                      notiList[idx] ? "justify-end" : null
                    }`}
                  >
                    <div
                      className={
                        notiList[idx] ? "active-toggle-bar" : "toggle-bar"
                      }
                    >
                      {null}
                    </div>
                    <div
                      className={
                        notiList[idx] ? "active-toggle-circle" : "toggle-circle"
                      }
                    >
                      {null}
                    </div>
                  </button>
                </div>
              );
            })}
          </div>
          <div className="main-noti__title fs-24 notoMid">공개 설정</div>
          <div className="main-noti__toggle flex">
            {followState.map((title, idx) => {
              const idxx = idx + 4;
              return (
                <div className="main-noti__toggle-item flex" key={v4()}>
                  <p className="notoReg fs-14">{title}</p>
                  <button
                    onClick={() => {
                      setNotiList(cur => {
                        const newArr = [...cur];
                        newArr[idxx] = !newArr[idxx];
                        return newArr;
                      });
                    }}
                    type="button"
                    className={`main-noti__toggle-btn flex ${
                      notiList[idxx] ? "justify-end" : null
                    }`}
                  >
                    <div
                      className={
                        notiList[idxx] ? "active-toggle-bar" : "toggle-bar"
                      }
                    >
                      {null}
                    </div>
                    <div
                      className={
                        notiList[idxx]
                          ? "active-toggle-circle"
                          : "toggle-circle"
                      }
                    >
                      {null}
                    </div>
                  </button>
                </div>
              );
            })}
          </div>
        </div>
      </div>
      {spinner ? (
        <div className="spinner-container">
          <img
            src={LoadingSpinner}
            className="loading-spinner"
            alt="로딩스피너"
          />
        </div>
      ) : (
        <div className="setaccount-submit flex fs-24 notoMid">
          <button
            className="setaccount-submit__submit"
            type="button"
            onClick={changeSet}
          >
            설정
          </button>
          <button
            onClick={goBack}
            className="setaccount-submit__cancle"
            type="button"
          >
            취소
          </button>
        </div>
      )}
    </div>
  );
}

export default React.memo(AccountSettingPage);
