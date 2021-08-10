package com.example.team5_final.network;

import androidx.annotation.Keep;

import java.util.Arrays;

@Keep
public class AddressResponse {
    AddressData[] addresses;

    @Keep
    private class AddressData {
        String roadAddress;
        AdressElement[] addressElements;

        @Keep
        private class AdressElement {
            String[] types;
            String longName;

            @Override
            public String toString() {
                return "AdressElement{" +
                        "types=" + Arrays.toString(types) +
                        ", longName='" + longName + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "AddressData{" +
                    "loadAddress='" + roadAddress + '\'' +
                    ", addressElements=" + Arrays.toString(addressElements) +
                    '}';
        }
    }

    @Override
    public String toString() {
        String postalCode="";

        if(addresses.length >= 1){
            for (AddressData.AdressElement element : addresses[0].addressElements){
                for (String type : element.types){
                    if (type.equals("POSTAL_CODE")){
                        postalCode = element.longName;
                        break;
                    }
                }
                if (!postalCode.equals("")){
                    break;
                }
            }

            return addresses[0].roadAddress+"\n"+postalCode;
        }

        return "";
    }
}