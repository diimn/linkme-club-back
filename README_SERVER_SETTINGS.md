## Настройка сервера Amazon EC2 (Amazon Linux)

### `Настройка прокси  nginx`
Необходимо проксировать запросы /api на порт приложения 
бэкэнда (5000 порт по умолчанию)
Все остальные запросы проксируются на фронт (3000 порт)

Зайти под рутом и поправить файл
````
nano /etc/nginx/conf.d/elasticbeanstalk/00_application.conf
location /api/ {
    proxy_pass         http://127.0.0.1:5000;
    proxy_http_version 1.1;
}
location / {
    proxy_pass          http://127.0.0.1:3000;
    proxy_http_version  1.1;
    proxy_set_header    Connection          $connection_upgrade;
    proxy_set_header    Upgrade             $http_upgrade;
    proxy_set_header    Host                $host;
    proxy_set_header    X-Real-IP           $remote_addr;
    proxy_s
````
````
nano /etc/nginx/nginx.conf
в блоке http
client_max_body_size 100M;
````

Выполнить команду

`service nginx restart`



### `Установка node js`
1) `curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash`
2) `. ~/.nvm/nvm.sh`
3) `nvm install node`


### `Запуск приложеня`
 Для запуска приложиния в фоновом режиме необходимо открыть 
 дополнительный screen
 Запуск осуществляется командой
 `npm start` 

**Note: Бэк устанавливается как Elastic BeansTalk Env**
### `Настройка окружения AWS Console`

####`Route 53`
![Pic1](readme_images/1.jpg?raw=true "Title")

####`EC2`
![Pic1](readme_images/1.jpg?raw=true "Title")

####`AMI`
![Pic1](readme_images/1.jpg?raw=true "Title")

####`RDS`
![Pic1](readme_images/1.jpg?raw=true "Title")

####`S3 storage`

![Pic1](readme_images/1.jpg?raw=true "Title")

####`Конфигурация nginx`



