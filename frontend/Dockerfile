FROM node:16.15-alpine3.15 as build
WORKDIR /build
COPY package*.json ./
RUN npm ci
COPY angular.json tsconfig.app.json tsconfig.json tsconfig.spec.json ./
COPY src src
RUN npm run build

FROM nginx:1.23.0-alpine
WORKDIR /usr/share/nginx/html
COPY --from=build /build/dist/test /usr/share/nginx/html
COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
