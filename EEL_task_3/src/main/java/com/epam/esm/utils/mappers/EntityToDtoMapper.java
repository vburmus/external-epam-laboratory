package com.epam.esm.utils.mappers;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.model.GiftCertificateDTO;
import com.epam.esm.order.model.Order;
import com.epam.esm.order.model.OrderDTO;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.model.TagDTO;
import com.epam.esm.user.model.User;
import com.epam.esm.user.model.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityToDtoMapper {
    GiftCertificateDTO toGiftCertificateDTO(GiftCertificate giftCertificate);

    GiftCertificate toGiftCertificate(GiftCertificateDTO giftCertificateDTO);

    User toUser(UserDTO userDTO);

    UserDTO toUserDTO(User user);

    TagDTO toTagDTO(Tag tag);

    Tag toTag(TagDTO tagDTO);

    OrderDTO toOrderDTO(Order order);

    Order toOrder(OrderDTO orderDTO);
}
