import React, { useState } from "react";
import "./JoinMore.scss";
import { v4 } from "uuid";
import { useNavigate } from "react-router-dom";
import { useDaumPostcodePopup } from "react-daum-postcode";
import { setUserMoreInfo } from "@apis/auth";
import LoadingSpinner from "@images/LoadingSpinner.svg";
import dealCategory from "@constants/dealCategory";

function JoinMore() {
  const navigate = useNavigate();
  const [address, setAddress] = useState<string>("");
  const [categorys, setCategorys] = useState<Array<string>>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const scriptUrl =
    "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
  const open = useDaumPostcodePopup(scriptUrl);
  const handleComplete = (data: any) => {
    console.log(data);
    let fullAddress = data.address;
    let extraAddress = "";

    if (data.addressType === "R") {
      if (data.bname !== "") {
        extraAddress += data.bname;
      }
      if (data.buildingName !== "") {
        extraAddress +=
          extraAddress !== "" ? `, ${data.buildingName}` : data.buildingName;
      }
      fullAddress += extraAddress !== "" ? ` (${extraAddress})` : "";
    }

    console.log(fullAddress); // e.g. '서울 성동구 왕십리로2길 20 (성수동1가)'
    setAddress(fullAddress);
  };

  const handleClick = () => {
    open({ onComplete: handleComplete });
  };

  const sendMoreInfo = async () => {
    if (!isLoading) {
      setIsLoading(true);
      const res = await setUserMoreInfo(address, categorys);
      if (res === "SUCCESS") {
        navigate("/");
      }
      setIsLoading(false);
    }
  };
  const toggleCategorys = (value: string) => {
    const index = categorys.indexOf(value);
    if (index === -1) {
      setCategorys([...categorys, value]);
    } else {
      setCategorys(categorys.filter(category => category !== value));
    }
  };

  const categoryClass = (value: string) => {
    const prefix =
      "categorys-ul__li flex align-center justify-center notoReg fs-11 ellipsis";
    if (categorys.indexOf(value) === -1) {
      return prefix;
    }
    return `${prefix} selected`;
  };
  return (
    <div className="wrapper">
      <div id="join-more">
        <header className="header">
          <p className="header__title notoBold fs-24">추가 선택사항</p>
          <p className="header__sub-title notoReg fs-16">
            추가 정보를 입력하시면
            <br />더 나은 서비스를 이용하실 수 있습니다.
          </p>
        </header>
        <main className="form">
          <p className="form__title notoBold fs-16">주소</p>
          <input
            type="text"
            className="form__input fs-15 notoReg"
            placeholder="주소를 검색해주세요"
            readOnly
            value={address}
          />
          <button
            type="button"
            className="form__btn fs-15 notoReg"
            onClick={handleClick}
          >
            도로명 주소 검색
          </button>
          <p className="form__title notoBold fs-16">관심 카테고리</p>
          <ul className="categorys-ul flex">
            {dealCategory.map(category => (
              <li className={categoryClass(category)} key={v4()}>
                <button type="button" onClick={() => toggleCategorys(category)}>
                  {category}
                </button>
              </li>
            ))}
          </ul>
          <button
            type="button"
            className="form__btn--submit notoMid fs-16 flex align-center justify-center"
            onClick={sendMoreInfo}
          >
            {isLoading ? (
              <img
                src={LoadingSpinner}
                className="loading-spinner"
                alt="로딩스피너"
              />
            ) : (
              "설정"
            )}
          </button>
        </main>
        {/* {modalVisible && <AddressModal onClose={() => setModalVisible(false)} />}
         */}
      </div>
    </div>
  );
}

export default JoinMore;
