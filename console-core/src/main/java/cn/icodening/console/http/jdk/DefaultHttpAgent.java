package cn.icodening.console.http.jdk;

import cn.icodening.console.http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author icodening
 * @date 2021.06.04
 */
public class DefaultHttpAgent implements HttpAgent {

    @Override
    public Response exchange(Request request, int timeout) throws IOException {
        String uri = request.getUri();
        Map<String, String> queryParams = request.getQueryParams();
        uri += buildQueryString(queryParams);

        URL url = new URL(uri);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(timeout);
        urlConnection.setRequestMethod(request.getMethod().toUpperCase());
        urlConnection.setConnectTimeout(300);

        HttpHeaders headers = request.getHeaders();
        if (headers != null) {
            headers.firstForeach(urlConnection::setRequestProperty);
        }

        byte[] requestBody = request.getBody();
        if (requestBody != null && requestBody.length != 0) {
            urlConnection.setDoOutput(true);
            try (OutputStream outputStream = urlConnection.getOutputStream()) {
                outputStream.write(requestBody);
                outputStream.flush();
            }
        }

        urlConnection.connect();

        try (InputStream inputStream = urlConnection.getInputStream();
             ByteArrayOutputStream bos = new ByteArrayOutputStream(1024)) {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.putAll(urlConnection.getHeaderFields());
            return new DefaultHttpResponse(urlConnection.getResponseCode(), httpHeaders, bos.toByteArray());
        }
    }

    private String buildQueryString(Map<String, String> queryParams) {
        StringBuilder queryStringBuilder = new StringBuilder();
        if (queryParams != null && queryParams.size() > 0) {
            queryStringBuilder.append("?");
            queryParams.forEach((key, value) -> {
                queryStringBuilder.append(key)
                        .append("=")
                        .append(value)
                        .append("&");
            });
            queryStringBuilder.deleteCharAt(queryStringBuilder.length() - 1);
        }
        return queryStringBuilder.toString();
    }


}
