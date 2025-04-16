package com.lanai.weather.service.impl;

import com.lanai.weather.Utils.ResData;
import com.lanai.weather.mapper.AddressMapper;
import com.lanai.weather.service.AddressService;
import com.lanai.weather.entity.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public ResData getAddress() {
        List<Address> addresses = addressMapper.selectList(null);
        return ResData.resultObject(addresses);
    }

    @Override
    public ResData delAddress(String id) {
        int i = addressMapper.deleteById(id);
        return ResData.resultInt(i);
    }

    @Override
    public ResData putAddress(Address address) {
        int i = addressMapper.updateById(address);
        return ResData.resultInt(i);
    }

    @Override
    public ResData addAddress(Address address) {
        int insert = addressMapper.insert(address);
        return ResData.resultInt(insert);
    }
}
