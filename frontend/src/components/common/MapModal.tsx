import React, { useState } from "react";
import kakaoMap from "@images/kakao_map.png";
import DealMap from "@components/common/DealMap";

function MapModal() {
  const [mapModal, setMapModal] = useState<boolean>(false);
  const targetUser = "ssafy";
  const showModal = () => {
    setMapModal(true);
  };
  const closeModal = () => {
    setMapModal(false);
  };
  return (
    <div id="mapmodal">
      <button type="button" onClick={showModal}>
        <img src={kakaoMap} alt="mapicon" className="mapmodal__icon" />
      </button>
      {mapModal ? (
        <DealMap targetUser={targetUser} closeModal={closeModal} />
      ) : null}
    </div>
  );
}

export default MapModal;
