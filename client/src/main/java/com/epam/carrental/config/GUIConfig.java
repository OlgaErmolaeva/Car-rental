package com.epam.carrental.config;

import com.epam.carrental.gui.models.UpdatableListComboBoxModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class GUIConfig {

    @Bean
    @SuppressWarnings("unchecked")
    public UpdatableListComboBoxModel updatableListComboBoxModel(){
        return new UpdatableListComboBoxModel(new ArrayList());
    }
}
