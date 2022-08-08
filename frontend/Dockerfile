FROM node:14.19.2 as builder

WORKDIR /app

COPY ./package.json ./

RUN npm i

COPY ./ ./

ARG KAKAO_ID
ARG NAVER_ID
ARG GOOGLE_ID

RUN REACT_APP_GOOGLE_CLIENT_ID=${GOOGLE_ID} REACT_APP_NAVER_CLIENT_ID=${NAVER_ID} REACT_APP_KAKAO_CLIENT_ID=${KAKAO_ID} npm run build 

FROM nginx

EXPOSE 80

COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf

COPY --from=builder /app/build /usr/share/nginx/html

CMD ["nginx","-g","daemon off;"]


