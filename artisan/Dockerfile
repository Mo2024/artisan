# Use the official Nginx image as the base
FROM nginx:latest

# Copy your Angular build output to the Nginx html folder
COPY ./dist/artisan/browser/ /usr/share/nginx/html/

COPY nginx.conf /etc/nginx/conf.d/default.conf

# Start Nginx in the foreground (necessary for Docker to keep the container running)
CMD ["nginx", "-g", "daemon off;"]
