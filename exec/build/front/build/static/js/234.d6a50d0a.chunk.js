"use strict";(self.webpackChunkfrontend=self.webpackChunkfrontend||[]).push([[234],{27594:function(e,t){t.Z=["\uc758\ub958","\uc2dd\ud488","\uc8fc\ubc29\uc6a9\ud488","\uc0dd\ud65c\uc6a9\ud488","\ud648\uc778\ud14c\ub9ac\uc5b4","\uac00\uc804\ub514\uc9c0\ud138","\ucde8\ubbf8\uc6a9\ud488","\uae30\ud0c0"]},97234:function(e,t,r){r.r(t),r.d(t,{default:function(){return v}});var n=r(42982),o=r(88214),c=r(15861),u=r(70885),i=r(72791),a=r(39575),l=r(16871),s=r(3767),f=r(39846),p=r(34108),d=r(27594),y=r(66579),b=r(67463),m=r(83958),h=r(80184);var v=function(){var e=(0,l.s0)(),t=(0,i.useState)(""),r=(0,u.Z)(t,2),v=r[0],O=r[1],j=(0,i.useState)([]),g=(0,u.Z)(j,2),w=g[0],P=g[1],_=(0,i.useState)(!1),S=(0,u.Z)(_,2),x=S[0],C=S[1],k=(0,y.C)((function(e){return e.auth.userInfo})),E=(0,y.T)(),N=(0,s.U)("//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"),M=function(e){var t=e.address,r="";"R"===e.addressType&&(""!==e.bname&&(r+=e.bname),""!==e.buildingName&&(r+=""!==r?", ".concat(e.buildingName):e.buildingName),t+=""!==r?" (".concat(r,")"):""),O(t)},R=function(){var t=(0,c.Z)((0,o.Z)().mark((function t(){return(0,o.Z)().wrap((function(t){for(;;)switch(t.prev=t.next){case 0:if(x){t.next=7;break}return C(!0),t.next=4,(0,f.Hw)(v,w);case 4:"SUCCESS"===t.sent&&(E((0,b.W0)({area:v,categorys:w})),e("/")),C(!1);case 7:case"end":return t.stop()}}),t)})));return function(){return t.apply(this,arguments)}}(),D=function(e){var t="categorys-ul__li flex align-center justify-center notoReg fs-11 ellipsis";return-1===w.indexOf(e)?t:"".concat(t," selected")};return(0,i.useEffect)((function(){(0,c.Z)((0,o.Z)().mark((function e(){return(0,o.Z)().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,E((0,m.U)());case 2:case"end":return e.stop()}}),e)})))()}),[]),(0,i.useEffect)((function(){k&&(P((null===k||void 0===k?void 0:k.categorys)||[]),O((null===k||void 0===k?void 0:k.area)||""))}),[k]),(0,h.jsx)("div",{className:"wrapper",children:(0,h.jsxs)("div",{id:"join-more",children:[(0,h.jsxs)("header",{className:"header",children:[(0,h.jsx)("p",{className:"header__title notoBold fs-24",children:"\ucd94\uac00 \uc120\ud0dd\uc0ac\ud56d"}),(0,h.jsxs)("p",{className:"header__sub-title notoReg fs-16",children:["\ucd94\uac00 \uc815\ubcf4\ub97c \uc785\ub825\ud558\uc2dc\uba74",(0,h.jsx)("br",{}),"\ub354 \ub098\uc740 \uc11c\ube44\uc2a4\ub97c \uc774\uc6a9\ud558\uc2e4 \uc218 \uc788\uc2b5\ub2c8\ub2e4."]})]}),(0,h.jsxs)("main",{className:"form",children:[(0,h.jsx)("p",{className:"form__title notoBold fs-16",children:"\uc8fc\uc18c"}),(0,h.jsx)("input",{type:"text",className:"form__input fs-15 notoReg",placeholder:"\uc8fc\uc18c\ub97c \uac80\uc0c9\ud574\uc8fc\uc138\uc694",readOnly:!0,value:v}),(0,h.jsx)("button",{type:"button",className:"form__btn fs-15 notoReg",onClick:function(){N({onComplete:M})},children:"\ub3c4\ub85c\uba85 \uc8fc\uc18c \uac80\uc0c9"}),(0,h.jsx)("p",{className:"form__title notoBold fs-16",children:"\uad00\uc2ec \uce74\ud14c\uace0\ub9ac"}),(0,h.jsx)("ul",{className:"categorys-ul flex",children:d.Z.map((function(e){return(0,h.jsx)("button",{type:"button",className:D(e),onClick:function(){return function(e){var t=w.indexOf(e);P(-1===t?[].concat((0,n.Z)(w),[e]):w.filter((function(t){return t!==e})))}(e)},children:e},(0,a.Z)())}))}),(0,h.jsx)("button",{type:"button",className:"form__btn--submit notoMid fs-16 flex align-center justify-center",onClick:R,children:x?(0,h.jsx)("img",{src:p.Z,className:"loading-spinner",alt:"\ub85c\ub529\uc2a4\ud53c\ub108"}):"\uc124\uc815"})]})]})})}},58261:function(e,t,r){function n(e){return n="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},n(e)}Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var o=a(r(72791)),c=a(r(97853)),u=["scriptUrl","className","style","defaultQuery","autoClose","errorMessage","onComplete","onClose","onResize","onSearch"];function i(e){if("function"!=typeof WeakMap)return null;var t=new WeakMap,r=new WeakMap;return(i=function(e){return e?r:t})(e)}function a(e,t){if(!t&&e&&e.__esModule)return e;if(null===e||"object"!==n(e)&&"function"!=typeof e)return{default:e};var r=i(t);if(r&&r.has(e))return r.get(e);var o={},c=Object.defineProperty&&Object.getOwnPropertyDescriptor;for(var u in e)if("default"!=u&&Object.prototype.hasOwnProperty.call(e,u)){var a=c?Object.getOwnPropertyDescriptor(e,u):null;a&&(a.get||a.set)?Object.defineProperty(o,u,a):o[u]=e[u]}return o.default=e,r&&r.set(e,o),o}function l(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function s(e){for(var t,r=1;r<arguments.length;r++)t=null==arguments[r]?{}:arguments[r],r%2?l(Object(t),!0).forEach((function(r){O(e,r,t[r])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):l(Object(t)).forEach((function(r){Object.defineProperty(e,r,Object.getOwnPropertyDescriptor(t,r))}));return e}function f(e,t){if(null==e)return{};var r,n,o=function(e,t){if(null==e)return{};var r,n,o={},c=Object.keys(e);for(n=0;n<c.length;n++)r=c[n],0<=t.indexOf(r)||(o[r]=e[r]);return o}(e,t);if(Object.getOwnPropertySymbols){var c=Object.getOwnPropertySymbols(e);for(n=0;n<c.length;n++)r=c[n],0<=t.indexOf(r)||Object.prototype.propertyIsEnumerable.call(e,r)&&(o[r]=e[r])}return o}function p(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}function d(e,t){for(var r,n=0;n<t.length;n++)(r=t[n]).enumerable=r.enumerable||!1,r.configurable=!0,"value"in r&&(r.writable=!0),Object.defineProperty(e,r.key,r)}function y(e,t){return y=Object.setPrototypeOf||function(e,t){return e.__proto__=t,e},y(e,t)}function b(e){var t=function(){if("undefined"==typeof Reflect||!Reflect.construct)return!1;if(Reflect.construct.sham)return!1;if("function"==typeof Proxy)return!0;try{return Boolean.prototype.valueOf.call(Reflect.construct(Boolean,[],(function(){}))),!0}catch(e){return!1}}();return function(){var r,n=v(e);if(t){var o=v(this).constructor;r=Reflect.construct(n,arguments,o)}else r=n.apply(this,arguments);return m(this,r)}}function m(e,t){return!t||"object"!==n(t)&&"function"!=typeof t?h(e):t}function h(e){if(void 0===e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return e}function v(e){return v=Object.setPrototypeOf?Object.getPrototypeOf:function(e){return e.__proto__||Object.getPrototypeOf(e)},v(e)}function O(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}var j=o.default.createElement("p",null,"\ud604\uc7ac Daum \uc6b0\ud3b8\ubc88\ud638 \uc11c\ube44\uc2a4\ub97c \uc774\uc6a9\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4. \uc7a0\uc2dc \ud6c4 \ub2e4\uc2dc \uc2dc\ub3c4\ud574\uc8fc\uc138\uc694."),g={width:"100%",height:400},w={scriptUrl:c.postcodeScriptUrl,errorMessage:j,autoClose:!0},P=function(e){function t(){var e;p(this,t);for(var n=arguments.length,c=Array(n),i=0;i<n;i++)c[i]=arguments[i];return O(h(e=r.call.apply(r,[this].concat(c))),"wrap",(0,o.createRef)()),O(h(e),"state",{hasError:!1}),O(h(e),"initiate",(function(t){if(e.wrap.current){var r=e.props,n=(r.scriptUrl,r.className,r.style,r.defaultQuery),o=r.autoClose,c=(r.errorMessage,r.onComplete),i=r.onClose,a=r.onResize,l=r.onSearch;new t(s(s({},f(r,u)),{},{oncomplete:function(t){c&&c(t),o&&e.wrap.current&&e.wrap.current.remove()},onsearch:l,onresize:a,onclose:i,width:"100%",height:"100%"})).embed(e.wrap.current,{q:n,autoClose:o})}})),O(h(e),"onError",(function(t){console.error(t),e.setState({hasError:!0})})),e}!function(e,t){if("function"!=typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function");e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,writable:!0,configurable:!0}}),t&&y(e,t)}(t,e);var r=b(t);return function(e,t,r){t&&d(e.prototype,t),r&&d(e,r)}(t,[{key:"componentDidMount",value:function(){var e=this.initiate,t=this.onError,r=this.props.scriptUrl;r&&(0,c.default)(r).then(e).catch(t)}},{key:"render",value:function(){var e=this.props,t=e.className,r=e.style,n=e.errorMessage,c=this.state.hasError;return o.default.createElement("div",{ref:this.wrap,className:t,style:s(s({},g),r)},c&&n)}}]),t}(o.Component);O(P,"defaultProps",w);var _=P;t.default=_},3767:function(e,t,r){Object.defineProperty(t,"U",{enumerable:!0,get:function(){return o.default}});var n=u(r(58261)),o=u(r(36550)),c=u(r(97853));function u(e){return e&&e.__esModule?e:{default:e}}n.default},97853:function(e,t){Object.defineProperty(t,"__esModule",{value:!0}),t.default=t.postcodeScriptUrl=void 0;var r="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";t.postcodeScriptUrl="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";var n=function(){var e=null;return function(){var t=0<arguments.length&&void 0!==arguments[0]?arguments[0]:r;return e||(e=new Promise((function(e,r){var n=document.createElement("script");n.src=t,n.onload=function(){var t,n;return null!==(t=window)&&void 0!==t&&null!==(n=t.daum)&&void 0!==n&&n.Postcode?e(window.daum.Postcode):void r(new Error("Script is loaded successfully, but cannot find Postcode module. Check your scriptURL property."))},n.onerror=function(e){return r(e)},n.id="daum_postcode_script",document.body.appendChild(n)})),e)}}(),o=n;t.default=o},36550:function(e,t,r){function n(e){return n="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},n(e)}Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var o=r(72791),c=function(e,t){if(!t&&e&&e.__esModule)return e;if(null===e||"object"!==n(e)&&"function"!=typeof e)return{default:e};var r=i(t);if(r&&r.has(e))return r.get(e);var o={},c=Object.defineProperty&&Object.getOwnPropertyDescriptor;for(var u in e)if("default"!=u&&Object.prototype.hasOwnProperty.call(e,u)){var a=c?Object.getOwnPropertyDescriptor(e,u):null;a&&(a.get||a.set)?Object.defineProperty(o,u,a):o[u]=e[u]}return o.default=e,r&&r.set(e,o),o}(r(97853)),u=["defaultQuery","left","top","popupKey","popupTitle","autoClose","onComplete","onResize","onClose","onSearch","onError"];function i(e){if("function"!=typeof WeakMap)return null;var t=new WeakMap,r=new WeakMap;return(i=function(e){return e?r:t})(e)}function a(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function l(e){for(var t,r=1;r<arguments.length;r++)t=null==arguments[r]?{}:arguments[r],r%2?a(Object(t),!0).forEach((function(r){s(e,r,t[r])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):a(Object(t)).forEach((function(r){Object.defineProperty(e,r,Object.getOwnPropertyDescriptor(t,r))}));return e}function s(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function f(e,t){if(null==e)return{};var r,n,o=function(e,t){if(null==e)return{};var r,n,o={},c=Object.keys(e);for(n=0;n<c.length;n++)r=c[n],0<=t.indexOf(r)||(o[r]=e[r]);return o}(e,t);if(Object.getOwnPropertySymbols){var c=Object.getOwnPropertySymbols(e);for(n=0;n<c.length;n++)r=c[n],0<=t.indexOf(r)||Object.prototype.propertyIsEnumerable.call(e,r)&&(o[r]=e[r])}return o}var p=function(){var e=0<arguments.length&&void 0!==arguments[0]?arguments[0]:c.postcodeScriptUrl;(0,o.useEffect)((function(){(0,c.default)(e)}),[e]);var t=(0,o.useCallback)((function(t){var r=l({},t),n=r.defaultQuery,o=r.left,i=r.top,a=r.popupKey,s=r.popupTitle,p=r.autoClose,d=r.onComplete,y=r.onResize,b=r.onClose,m=r.onSearch,h=r.onError,v=f(r,u);return(0,c.default)(e).then((function(e){new e(l(l({},v),{},{oncomplete:d,onsearch:m,onresize:y,onclose:b})).open({q:n,left:o,top:i,popupTitle:s,popupKey:a,autoClose:p})})).catch(h)}),[e]);return t};t.default=p}}]);
//# sourceMappingURL=234.d6a50d0a.chunk.js.map