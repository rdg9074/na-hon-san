"use strict";(self.webpackChunkfrontend=self.webpackChunkfrontend||[]).push([[989],{29989:function(e,n,t){t.r(n),t.d(n,{default:function(){return Z}});var s=t(88214),r=t(15861),a=t(70885),i=t(72791),c=t(20501),l=t(16871),o=t(64826),u=t(8503),d=t(51988),m=t(6993),f=t(93561),x=t(86135),h=t(66579),p=t(81121),j=t(194),v=t(20343),k=t(79343),b=t(7512),N=t(22985),g=t(80184);var Z=function(){var e=(0,i.useState)(!1),n=(0,a.Z)(e,2),t=n[0],Z=n[1],w=(0,i.useState)(""),y=(0,a.Z)(w,2),C=y[0],_=y[1],S=(0,i.useState)(),I=(0,a.Z)(S,2),M=I[0],F=I[1],U=(0,i.useState)(!1),L=(0,a.Z)(U,2),R=L[0],T=L[1],z=(0,i.useState)(),A=(0,a.Z)(z,2),D=A[0],E=A[1],P=(0,l.UO)().id,H=(0,i.useState)({isFollow:!1,isLike:!1}),K=(0,a.Z)(H,2),O=K[0],V=K[1],q=(0,h.C)((function(e){return e.auth.userInfo})),B=(null===q||void 0===q?void 0:q.nickname)===(null===M||void 0===M?void 0:M.userNickname),G=(0,l.s0)(),J=function(){Z((function(e){return!e}))};(0,i.useEffect)((function(){(0,p.aV)(P).then((function(e){if(F(e.deal),e.dealComments){var n=e.dealComments.reverse();E(n)}_(e.deal.state),V({isFollow:e.isFollow,isLike:e.isLike})}))}),[t,P]);var Q=function(){var e=(0,r.Z)((0,s.Z)().mark((function e(){var n;return(0,s.Z)().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:if(!window.confirm("\uc815\ub9d0\ub85c \uc0ad\uc81c\ud558\uc2dc\uaca0\uc2b5\ub2c8\uae4c?")){e.next=7;break}return e.next=4,(0,p.Sz)(P);case 4:return"SUCCESS"===(n=e.sent)&&G("/"),e.abrupt("return",n);case 7:return e.abrupt("return",0);case 8:case"end":return e.stop()}}),e)})));return function(){return e.apply(this,arguments)}}();if(!M)return(0,g.jsx)("div",{});var W,X=function(){var e=(0,r.Z)((0,s.Z)().mark((function e(){return(0,s.Z)().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:if(q){e.next=2;break}return e.abrupt("return",G("/login"));case 2:if(R){e.next=9;break}return T(!0),e.next=6,(0,p.eM)(P);case 6:200===e.sent.status&&J(),T(!1);case 9:return e.abrupt("return",0);case 10:case"end":return e.stop()}}),e)})));return function(){return e.apply(this,arguments)}}(),Y=function(){var e=(0,r.Z)((0,s.Z)().mark((function e(){return(0,s.Z)().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:if(q){e.next=2;break}return e.abrupt("return",G("/login"));case 2:if(R){e.next=13;break}if(T(!0),!O.isFollow){e.next=9;break}return e.next=7,(0,k.zM)(M.userNickname);case 7:e.next=11;break;case 9:return e.next=11,(0,k.R3)(M.userNickname);case 11:T(!1),J();case 13:return e.abrupt("return",0);case 14:case"end":return e.stop()}}),e)})));return function(){return e.apply(this,arguments)}}();return(0,g.jsx)("div",{id:"deal-detail-page",children:(0,g.jsxs)("div",{className:"article flex column",children:[(0,g.jsx)("p",{className:"title notoMid",children:M.title}),(0,g.jsx)("div",{className:"title-state_info",children:B&&(0,g.jsx)("p",{className:"notoReg fs-10",children:"\uac70\ub798 \uc0c1\ud0dc\ub294 \uc218\uc815\ud398\uc774\uc9c0\uc5d0\uc11c\ub9cc \ubcc0\uacbd \uac00\ub2a5\ud574\uc694"})}),(0,g.jsxs)("div",{className:"header flex",children:[(0,g.jsxs)("div",{className:"header-info flex",children:[(0,g.jsx)("div",{className:"header-info__img-container flex",children:(0,g.jsx)("button",{type:"button",onClick:function(){G("/userfeed/".concat(M.userNickname))},children:(0,g.jsx)("img",{src:M.userProfileImg?"data:image/jpeg;base64,".concat(M.userProfileImg):o.Z,alt:"User",className:"profile-user__img",title:"User"})})}),(0,g.jsxs)("div",{className:"header-info__text flex column",children:[(0,g.jsx)(c.rU,{to:"/userfeed/".concat(M.userNickname),className:"user-name notoMid",children:M.userNickname}),(0,g.jsx)("div",{className:"created flex column align-center",children:(0,g.jsx)("p",{className:" notoReg",children:M.updateTime?"".concat((0,x.h)(M.updateTime)," (\uc218\uc815\ub428)"):(0,x.h)(M.time)})})]}),(0,g.jsx)("button",{onClick:Y,className:"header-info__btn notoReg ".concat(B&&"hide"," ").concat(O.isFollow?"grey":"yellow"),type:"button",children:O.isFollow?"\uc5b8\ud314\ub85c\uc6b0":"\ud314\ub85c\uc6b0"})]}),(0,g.jsx)("div",{className:"header-func flex",children:B?(0,g.jsxs)("div",{className:"header-func-btn flex",children:[(0,g.jsx)("div",{className:"header-func-btn_state flex justify-center",children:(0,g.jsx)("div",{className:"notoReg fs-10 ".concat((W=C,"\uac70\ub798 \ub300\uae30"===W?"green":"\uac70\ub798 \uc9c4\ud589"===W?"yellow":"brown"))})}),(0,g.jsx)("button",{onClick:function(){G("/deal/edit/".concat(P),{state:M})},type:"button",children:(0,g.jsx)("img",{src:m.Z,alt:"edit",title:"edit"})}),(0,g.jsx)("button",{onClick:Q,type:"button",children:(0,g.jsx)("img",{src:f.Z,alt:"del",title:"delete"})})]}):(0,g.jsxs)("div",{className:"header-func-btn flex",children:[q?(0,g.jsx)(N.Z,{targetUser:M.userNickname}):(0,g.jsx)("button",{type:"button",onClick:function(){G("/login")},children:(0,g.jsx)("img",{src:d,alt:"KakaoMap"})}),(0,g.jsx)("button",{onClick:X,type:"button",children:(0,g.jsx)("img",{src:O.isLike?b.Z:u.Z,alt:"like",title:"like"})})]})})]}),(0,g.jsx)("div",{className:"body flex",children:(0,g.jsx)("div",{className:"body-content ",dangerouslySetInnerHTML:{__html:M.content}})}),(0,g.jsxs)("div",{className:"comment flex column",children:[(0,g.jsx)("div",{className:"comment-head",children:(0,g.jsxs)("p",{className:"notoMid",children:["\ub313\uae00",(0,g.jsx)("span",{className:"",children:M.comment})]})}),(0,g.jsxs)("div",{className:"comment-input flex",children:[(0,g.jsx)("div",{className:"input-img-container flex",children:(0,g.jsx)("img",{src:null!==q&&void 0!==q&&q.profileImg?"data:image/jpeg;base64,".concat(q.profileImg):o.Z,alt:"dum",title:"user-icon"})}),(0,g.jsx)(v.Z,{type:"Deal",changed:J,articleIdx:P})]}),D?(0,g.jsx)(j.Z,{isArticleAuthor:M.userNickname===(null===q||void 0===q?void 0:q.nickname),postIdx:P,changed:J,type:"Deal",comments:D}):null]})]})})}}}]);
//# sourceMappingURL=989.a5b877c7.chunk.js.map