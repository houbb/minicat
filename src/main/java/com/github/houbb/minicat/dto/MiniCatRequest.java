package com.github.houbb.minicat.dto;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @since 0.2.0
 */
public class MiniCatRequest extends MiniCatRequestAdaptor {

    private static final Log logger = LogFactory.getLog(MiniCatRequest.class);

    /**
     * 请求方式 例如：GET/POST
     */
    private String method;


    /**
     * / ， /index.html
     */

    private String url;


    /**
     * 其他的属性都是通过inputStream解析出来的。
     */

    private final InputStream inputStream;

    public MiniCatRequest(InputStream inputStream) {
        this.inputStream = inputStream;

        this.readFromStreamByBuffer();
    }


    /**
     * 直接根据 available 有时候读取不到数据
     * @since 0.3.0
     */
    private void readFromStreamByBuffer() {
        byte[] buffer = new byte[1024]; // 使用固定大小的缓冲区
        int bytesRead = 0;

        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) { // 循环读取数据直到EOF
                String inputStr = new String(buffer, 0, bytesRead);

                // 检查是否读取到完整的HTTP请求行
                if (inputStr.contains("\n")) {
                    // 获取第一行数据
                    String firstLineStr = inputStr.split("\\n")[0];
                    String[] strings = firstLineStr.split(" ");
                    this.method = strings[0];
                    this.url = strings[1];

                    logger.info("[MiniCat] method={}, url={}", method, url);
                    break; // 退出循环，因为我们已经读取到请求行
                }
            }

            if ("".equals(method)) {
                logger.info("[MiniCat] No HTTP request line found, ignoring.");
                // 可以选择抛出异常或者返回空请求对象
            }
        } catch (IOException e) {
            logger.error("[MiniCat] readFromStream meet ex", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 您遇到的问题是在某些情况下，`inputStream.available()` 返回的 `count` 为 0，导致后续的 `read` 方法调用无法读取任何数据。
     *
     * 这个问题可能与网络流的特性和 `available()` 方法的实现有关。
     *
     * 根据搜索结果【3】，网络流（如 Socket 流）与文件流不同，网络流的 `available()` 方法可能返回 0，即使实际上有数据可读。这是因为网络通讯是间断性的，数据可能分多个批次到达。
     *
     * 例如，对方发送的字节长度为100的数据，本地程序调用 `available()` 方法有时得到0，有时得到50，有时能得到100，大多数情况下是0。
     *
     * 这是因为网络传输层可能会将较长的数据包进行分割，而且数据的到达可能存在延迟。
     *
     * 为了解决这个问题，您可以采取以下措施：
     *
     * 1. **避免直接依赖 `available()` 方法**：由于 `available()` 方法在网络流中可能不准确，您可以尝试不使用此方法来预分配字节数组。相反，您可以使用一个固定大小的缓冲区，或者使用 `read()` 方法的循环来动态读取数据。
     *
     * 2. **使用循环读取数据**：您可以在一个循环中使用 `read()` 方法逐字节读取数据，直到读取到换行符（表示 HTTP 请求行的结束）或其他特定的分隔符。这种方法不依赖于 `available()` 方法，可以确保即使数据分批到达也能正确读取。
     *
     * 3. **处理粘包和拆包问题**：由于 TCP 流式传输的特性，您可能会遇到所谓的“粘包”和“拆包”问题。您可以通过在协议中定义明确的分隔符或使用特定的编码方式来处理这些问题。
     *
     * 4. **调整缓冲区大小**：您可以尝试调整读取数据时使用的缓冲区大小。较大的缓冲区可能有助于减少因数据分批到达导致的 `available()` 方法返回 0 的情况。
     *
     * 5. **错误处理和重试机制**：在读取数据时，如果遇到 `available()` 返回 0 或其他错误情况，您可以实现错误处理和重试机制。例如，您可以设置一个重试次数，如果读取失败，则在等待一段时间后重试。
     *
     * 6. **优化日志记录**：在您的代码中，如果 `readResult` 为 0，则记录了一条日志并返回。这可能不是最佳实践，因为它可能导致有用的请求被忽略。您可以记录更详细的错误信息，并考虑在这种情况下采取不同的行动，例如重试读取或发送一个错误响应给客户端。
     *
     * 通过上述措施，您可以提高您的服务器代码的健壮性，确保即使在网络条件不稳定的情况下也能正确处理 HTTP 请求。
     */
    @Deprecated
    private void readFromStream() {
        try {
            //从输入流中获取请求信息
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            int readResult = inputStream.read(bytes);
            String inputsStr = new String(bytes);
            logger.info("[MiniCat] readCount={}, input stream {}", readResult, inputsStr);
            if(readResult <= 0) {
                logger.info("[MiniCat] readCount is empty, ignore handle.");
                return;
            }

            //获取第一行数据
            String firstLineStr = inputsStr.split("\\n")[0];  //GET / HTTP/1.1
            String[] strings = firstLineStr.split(" ");
            this.method = strings[0];
            this.url = strings[1];

            logger.info("[MiniCat] method={}, url={}", method, url);
        } catch (IOException e) {
            logger.error("[MiniCat] readFromStream meet ex", e);
            throw new RuntimeException(e);
        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "MiniCatRequest{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", inputStream=" + inputStream +
                "} " + super.toString();
    }

}
