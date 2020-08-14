# etapa de compilación
FROM node:12.18.3-alpine3.9 as build-stage
WORKDIR /apps
COPY package.json yarn.lock /apps/
RUN yarn install
ADD . /apps
RUN yarn run build

# etapa de producción
FROM nginx:1.19.1-alpine as production-stage
COPY --from=build-stage /apps/dist /usr/share/nginx/html
EXPOSE 80
COPY entrypoint.sh /
CMD ["/entrypoint.sh"]