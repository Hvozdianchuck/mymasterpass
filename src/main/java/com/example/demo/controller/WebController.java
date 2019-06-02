package com.example.demo.controller;


import com.example.demo.TestPaymant;
import com.mastercard.merchant.checkout.PaymentDataApi;
import com.mastercard.merchant.checkout.model.Address;
import com.mastercard.merchant.checkout.model.PaymentData;
import com.mastercard.sdk.core.util.QueryParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@Controller
public class WebController extends BaseController  {

    @Value("${masterpass.merchant.checkoutId}")
   private String merchantCheckoutId;

    @Value("${masterpass.merchant.callbackUrl}")
   private String callBackUrl;
private String cartId;
    private String oauth_verifie;
    @RequestMapping("/home")
  public   ModelAndView home() {

        ModelAndView mv = new ModelAndView("home");
        return mv;
    }


    @RequestMapping("/checkout")
  public   ModelAndView checkout(@RequestParam("mpstatus") String status, @RequestParam(value = "oauth_token", required = false) String oauthToken,
                          @RequestParam(value = "oauth_verifier", required = false) String oauthVerifier,
                          @RequestParam(value = "checkout_resource_url", required = false) String checkoutResourceUrl, HttpSession httpSession) {

        if(status.equals("success")){
            oauth_verifie=oauthVerifier;
            httpSession.setAttribute("oauth_verifier",oauthVerifier);
        }

        return new ModelAndView("redirect:success");
    }

    @RequestMapping("/standardCheckout")
 public    ModelAndView standardCheckout(HttpSession httpSession) {

        ModelAndView mv = new ModelAndView("standardCheckout");


          cartId = UUID.randomUUID().toString();
        System.out.println(cartId);

        httpSession.setAttribute("cart_id",cartId);


        mv.addObject("cart_id",cartId);
        mv.addObject("checkout_id",merchantCheckoutId);
        mv.addObject("callback_url",callBackUrl);

        return mv;



    }

    @RequestMapping("/success")
   public ModelAndView success(HttpSession httpSession) {

        ModelAndView mv = new ModelAndView("success");
        mv.addObject("transactionId",httpSession.getAttribute("oauth_verifier"));

        System.out.println("null " );
        System.out.println(cartId);
        System.out.println("work " );
        QueryParams queryParams = new QueryParams()
                .add("checkoutId", merchantCheckoutId)
                .add("cartId", cartId);
        System.out.println(queryParams.toString());
        System.out.println(httpSession.getAttribute("oauth_verifier").toString());
        PaymentData paymentData = TestPaymant.showTest(httpSession.getAttribute("oauth_verifier").toString(), queryParams);
//        PaymentData paymentData = PaymentDataApi.show(httpSession.getAttribute("oauth_verifier").toString(), queryParams);
        System.out.println("Card number is "+paymentData.getCard().getAccountNumber());
        Address shippingAdd = paymentData.getShippingAddress();

        List<String> shippingAddress = new ArrayList<>();

        if(shippingAdd != null){

            if(shippingAdd.getLine1() != null){

                shippingAddress.add(shippingAdd.getLine1());

            }
            if(shippingAdd.getLine2() != null){

                shippingAddress.add(shippingAdd.getLine2());

            }
            if(shippingAdd.getLine3() != null){

                shippingAddress.add(shippingAdd.getLine3());

            }

            if(shippingAdd.getCity() != null){

                shippingAddress.add(shippingAdd.getCity());


            }

            if(shippingAdd.getCountry() != null){

                shippingAddress.add(shippingAdd.getCountry());
            }

            if(shippingAdd.getPostalCode()!=null){
                shippingAddress.add(shippingAdd.getPostalCode());
            }

        }

        mv.addObject("shippingAddress",shippingAddress);

        return mv;
    }
}
