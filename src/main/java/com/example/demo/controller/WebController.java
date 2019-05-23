package com.example.demo.controller;


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



@RestController
public class WebController   {

    @Value("${masterpass.merchant.checkoutId}")
   private String merchantCheckoutId;

    @Value("${masterpass.merchant.callbackUrl}")
   private String callBackUrl;

    @RequestMapping("/home")
    ModelAndView home() {

        ModelAndView mv = new ModelAndView("home");
        return mv;
    }


    @RequestMapping("/checkout")
    ModelAndView checkout(@RequestParam("mpstatus") String status, @RequestParam(value = "oauth_token", required = false) String oauthToken,
                          @RequestParam(value = "oauth_verifier", required = false) String oauthVerifier,
                          @RequestParam(value = "checkout_resource_url", required = false) String checkoutResourceUrl, HttpSession httpSession) {

        if(status.equals("success")){

            httpSession.setAttribute("oauth_verifier",oauthVerifier);
        }

        return new ModelAndView("redirect:success");
    }

    @RequestMapping("/standardCheckout")
    ModelAndView standardCheckout(HttpSession httpSession) {

        ModelAndView mv = new ModelAndView("standardCheckout");


        String cartId = UUID.randomUUID().toString();

        httpSession.setAttribute("cart_id",cartId);


        mv.addObject("cart_id",cartId);
        mv.addObject("checkout_id",merchantCheckoutId);
        mv.addObject("callback_url",callBackUrl);

        return mv;


    }

    @RequestMapping("/success")
    ModelAndView success(HttpSession httpSession) {

        ModelAndView mv = new ModelAndView("success");
        mv.addObject("transactionId",httpSession.getAttribute("oauth_verifier"));


        QueryParams queryParams = new QueryParams()
                .add("checkoutId", merchantCheckoutId)
                .add("cartId", httpSession.getAttribute("cart_id").toString());

        PaymentData paymentData = PaymentDataApi.show(httpSession.getAttribute("oauth_verifier").toString(), queryParams);
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
