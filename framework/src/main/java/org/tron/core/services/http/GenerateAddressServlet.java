package org.ypbay.core.services.http;

import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import org.ypbay.common.crypto.SignInterface;
import org.ypbay.common.crypto.SignUtils;
import org.ypbay.common.utils.ByteArray;
import org.ypbay.common.utils.StringUtil;
import org.ypbay.common.utils.Utils;
import org.ypbay.core.config.args.Args;


@Component
@Slf4j(topic = "API")
public class GenerateAddressServlet extends RateLimiterServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    try {
      SignInterface sign = SignUtils.getGeneratedRandomSign(Utils.getRandom(),
          Args.getInstance().isECKeyCryptoEngine());
      byte[] priKey = sign.getPrivateKey();
      byte[] address = sign.getAddress();
      String priKeyStr = Hex.encodeHexString(priKey);
      String base58check = StringUtil.encode58Check(address);
      String hexString = ByteArray.toHexString(address);
      JSONObject jsonAddress = new JSONObject();
      jsonAddress.put("address", base58check);
      jsonAddress.put("hexAddress", hexString);
      jsonAddress.put("privateKey", priKeyStr);
      response.getWriter().println(jsonAddress.toJSONString());
    } catch (Exception e) {
      Util.processError(e, response);
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    doGet(request, response);
  }
}