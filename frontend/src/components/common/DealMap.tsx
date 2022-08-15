import React, { useState, useEffect, useRef } from "react";
import "./DealMap.scss";
import loadingSpinner from "@images/LoadingSpinner.svg";
import exit from "@images/X.svg";
import { dealMap } from "@apis/honeyDeal";

interface DealMapProps {
  closeModal: () => void;
  targetUser: string;
}

type positionType = {
  positionY: number;
  positionX: number;
};

type resultType = {
  targetUserTotalTime: number;
  finalBusPositionX: number;
  loginUserTotalTime: number;
  finalBusPositionY: number;
};

type midPositionType = {
  result: resultType | null;
  distacn: number | null;
  radius: number;
  midXPosition: number;
  midYPosition: number;
  loginUserTimeList: Array<number> | null;
  targetUserTimeList: Array<number> | null;
  busStationList: Array<Array<number>> | null;
};

function DealMap({ closeModal, targetUser }: DealMapProps) {
  const { kakao } = window as any;
  const mapRef = useRef(null);
  const [isLoading, setIsLoading] = useState(false);
  const [loginPosition, setLoginPosition] = useState<positionType | null>(null);
  const [targetPosition, setTargetPosition] = useState<positionType | null>(
    null
  );
  const [midPosition, setMidPosition] = useState<midPositionType | null>(null);

  const makeMap = () => {
    if (!midPosition) return;
    if (!loginPosition || !targetPosition) return;
    const container = mapRef.current;
    const options = {
      center: new kakao.maps.LatLng(
        midPosition.midYPosition,
        midPosition.midXPosition
      ),
      level: 2
    };

    const map = new kakao.maps.Map(container, options);
    const loginUserPosi = new kakao.maps.LatLng(
      loginPosition.positionY,
      loginPosition.positionX
    );
    const targetUserPosi = new kakao.maps.LatLng(
      targetPosition.positionY,
      targetPosition.positionX
    );

    const imgSrc = "https://i.ibb.co/8NBcFrw/honeybee.png";
    const imgSize = new kakao.maps.Size(64, 69);
    const imgOption = { offset: new kakao.maps.Point(34, 44) };
    const mdMarkerImg = new kakao.maps.MarkerImage(imgSrc, imgSize, imgOption);
    const mdPosi = new kakao.maps.LatLng(
      midPosition.midYPosition,
      midPosition.midXPosition
    );

    const loginMarkerImgSrc = "https://i.ibb.co/dgZDp51/user.png";
    const loginMarkerImgSize = new kakao.maps.Size(44, 44);
    const loginMarkerImgOption = { offset: new kakao.maps.Point(0, 0) };
    const loginMarkerImg = new kakao.maps.MarkerImage(
      loginMarkerImgSrc,
      loginMarkerImgSize,
      loginMarkerImgOption
    );

    const targetMarkerImgSrc = "https://i.ibb.co/WxSxPQF/user-1.png";
    const targetMarkerImgSize = new kakao.maps.Size(44, 44);
    const targetMarkerImgOption = { offset: new kakao.maps.Point(0, 0) };
    const targetMarkerImg = new kakao.maps.MarkerImage(
      targetMarkerImgSrc,
      targetMarkerImgSize,
      targetMarkerImgOption
    );

    const loginMarker = new kakao.maps.Marker({
      position: loginUserPosi,
      image: loginMarkerImg
    });
    const targetMarker = new kakao.maps.Marker({
      position: targetUserPosi,
      image: targetMarkerImg
    });
    const mdMarker = new kakao.maps.Marker({
      position: mdPosi,
      image: mdMarkerImg
    });

    loginMarker.setMap(map);
    targetMarker.setMap(map);
    mdMarker.setMap(map);

    const loginIwContent =
      '<div class="login-info">' +
      '  <div class="blank" target="_blank">' +
      '    <span class="title">나의 위치!</span>' +
      "  </div>" +
      "</div>";

    const loginInfoWindow = new kakao.maps.CustomOverlay({
      position: loginUserPosi,
      content: loginIwContent
    });

    const targetIwContent =
      '<div class="target-info">' +
      '  <div class="blank" target="_blank">' +
      '    <span class="title">상대방의 위치!</span>' +
      "  </div>" +
      "</div>";

    const targetInfoWindow = new kakao.maps.CustomOverlay({
      position: targetUserPosi,
      content: targetIwContent
    });
    loginInfoWindow.setMap(map);
    targetInfoWindow.setMap(map);

    if (midPosition.busStationList && midPosition.result) {
      const imageSrc = "https://i.ibb.co/NpV92mb/bus-stop.png";
      const imageSize = new kakao.maps.Size(55, 55);
      const overImageSize = new kakao.maps.Size(77, 77);
      const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
      const overMarkerImage = new kakao.maps.MarkerImage(
        imageSrc,
        overImageSize
      );
      const marker = new kakao.maps.Marker({
        map,
        position: new kakao.maps.LatLng(
          midPosition.result.finalBusPositionY,
          midPosition.result.finalBusPositionX
        ),
        image: markerImage
      });
      const iwContentMain = `<div style="padding:5px;">소요 시간 <br> 나의 기준 ${midPosition.result.loginUserTotalTime}분 <br> 상대 기준 ${midPosition.result.targetUserTotalTime}분 <br>여기까지 <a href="https://map.kakao.com/link/to/목적 정류장!,${midPosition.result.finalBusPositionY},${midPosition.result.finalBusPositionX}" style="color:blue" target="_blank">길찾기</a></div>`;
      const iwRemoveable = true;

      const infowindowMain = new kakao.maps.InfoWindow({
        content: iwContentMain,
        removable: iwRemoveable
      });
      kakao.maps.event.addListener(marker, "click", () =>
        infowindowMain.open(map, marker)
      );
      kakao.maps.event.addListener(marker, "mouseover", () =>
        marker.setImage(overMarkerImage)
      );

      kakao.maps.event.addListener(marker, "mouseout", () =>
        marker.setImage(markerImage)
      );

      const busStations = midPosition.busStationList.filter(
        position =>
          position[0] !== midPosition.result?.finalBusPositionX &&
          position[1] !== midPosition.result?.finalBusPositionY
      );
      for (let i = 0; i < busStations.length; i += 1) {
        const imageSrcBus = "https://i.ibb.co/jLvfz5W/bus-stop-1.png";
        const imageSizeBus = new kakao.maps.Size(35, 35);
        const overImageSizeBus = new kakao.maps.Size(44, 44);
        const markerImageBus = new kakao.maps.MarkerImage(
          imageSrcBus,
          imageSizeBus
        );
        const overMarkerImageBus = new kakao.maps.MarkerImage(
          imageSrcBus,
          overImageSizeBus
        );
        const markerBus = new kakao.maps.Marker({
          map,
          position: new kakao.maps.LatLng(
            midPosition.busStationList[i][1],
            midPosition.busStationList[i][0]
          ),
          image: markerImageBus
        });

        const contentMain = `<div style="padding:5px;">소요 시간 <br> 나의 기준 ${midPosition?.loginUserTimeList?.[i]} <br> 상대 기준${midPosition?.targetUserTimeList?.[i]} <br>여기까지 <a href="https://map.kakao.com/link/to/목적 정류장!,${midPosition.busStationList[i][1]},${midPosition.busStationList[i][0]}" style="color:blue" target="_blank">길찾기</a></div>`;

        const iwRemoveableBus = true;

        const infowindowBusMain = new kakao.maps.InfoWindow({
          content: contentMain,
          removable: iwRemoveableBus
        });
        kakao.maps.event.addListener(markerBus, "click", () => {
          infowindowBusMain.open(map, markerBus);
        });

        kakao.maps.event.addListener(markerBus, "mouseover", () => {
          markerBus.setImage(overMarkerImageBus);
        });

        kakao.maps.event.addListener(markerBus, "mouseout", () => {
          markerBus.setImage(markerImageBus);
        });
      }
    }
    if (midPosition.radius) {
      const circle = new kakao.maps.Circle({
        center: new kakao.maps.LatLng(
          midPosition.midYPosition,
          midPosition.midXPosition
        ),
        radius: midPosition.radius, // 미터 단위의 원의 반지름입니다
        strokeWeight: 3, // 선의 두께입니다
        strokeColor: "#FFBF00", // 선의 색깔입니다
        strokeOpacity: 0.7, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
        strokeStyle: "dashed", // 선의 스타일 입니다
        fillColor: "#FFBF00", // 채우기 색깔입니다
        fillOpacity: 0.2 // 채우기 불투명도 입니다
      });

      circle.setMap(map);
    }

    const bounds = new kakao.maps.LatLngBounds();
    bounds.extend(loginUserPosi);
    bounds.extend(targetUserPosi);
    bounds.extend(mdPosi);
    map.setBounds(bounds);
  };
  useEffect(() => {
    if (!isLoading) {
      (async () => {
        const res = await dealMap(targetUser as string);
        if (res.data.message === "SUCCESS") {
          setLoginPosition(res.data.loginUserPosition);
          setTargetPosition(res.data.targetUserPosition);
          setMidPosition(res.data.midPositionInfo);
          setIsLoading(true);
        } else if (res.data.message === "targetUserPositionNotFound") {
          closeModal();
          alert("상대방 주소 없어요.");
        } else if (res.data.message === "loginUserPositionNotFound") {
          closeModal();
          alert("주소를 등록해주세요!!");
        }
      })();
    } else {
      makeMap();
    }
  }, [isLoading]);

  return (
    <div id="dealmap">
      <div className="container">
        <div className="map">
          <div className="map-exit">
            <button type="button" onClick={closeModal}>
              <img src={exit} alt="exit" />
            </button>
          </div>
          <button
            className="map-sight flex"
            ref={mapRef}
            type="button"
            onClick={e => e.stopPropagation()}
          />
          <div className="main-info">
            <div className="red-bus flex">
              <img src="https://i.ibb.co/NpV92mb/bus-stop.png" alt="" />
              <p>가장 가까운 정류장</p>
            </div>
            <div className="yellow-bus flex">
              <img src="https://i.ibb.co/jLvfz5W/bus-stop-1.png" alt="" />
              <p>만나기 좋은 정류장</p>
            </div>
          </div>
        </div>
      </div>
      <button className="dimmer" type="button" onClick={closeModal} />
    </div>
  );
}

export default DealMap;