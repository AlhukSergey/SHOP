package by.teachmeskills.shop.utils;

import by.teachmeskills.shop.domain.OrderStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConverterUtils {
    public static OrderStatus toOrderStatus(String status) {
        return status.equalsIgnoreCase("ACTIVE") ? OrderStatus.ACTIVE : OrderStatus.FINISHED;
    }
}
