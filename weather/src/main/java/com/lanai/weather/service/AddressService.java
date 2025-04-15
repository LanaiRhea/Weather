package com.lanai.weather.service;

import com.lanai.weather.Utils.ResData;
import com.lanai.weather.entity.Address;

/**
 */
public interface AddressService {
    ResData getAddress();

    ResData delAddress(String id);

    ResData putAddress(Address address);

    ResData addAddress(Address address);
}
