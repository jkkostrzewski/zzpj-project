package com.zachaczcompany.zzpj.security.jwt;

import com.zachaczcompany.zzpj.security.auth.database.UserEntity;
import com.zachaczcompany.zzpj.shops.domain.Shop;

class UserCreatedDTO {
    public final long id;
    public final String username;
    public final Shop shop;

    UserCreatedDTO(UserEntity userEntity) {
        id = userEntity.getId();
        username = userEntity.getUsername();
        shop = userEntity.getShop();
    }
}
