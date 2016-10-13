package com.epam.carrental.gui.utils;

import com.epam.carrental.dto.ServerInfoDTO;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class MessageFormatter {

    public static String prepareMessageFromServerInfoDTO(ServerInfoDTO serverInfoDTO) {
        String time = serverInfoDTO.getTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        String ipAddress = serverInfoDTO.getIpAddress().getHostAddress();
        return "Server returned " + ipAddress + " IP address. Server time is " + time;
    }
}