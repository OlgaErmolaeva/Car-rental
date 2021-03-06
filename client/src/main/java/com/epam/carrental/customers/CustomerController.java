package com.epam.carrental.customers;

import com.epam.carrental.dto.CustomerDTO;
import com.epam.carrental.gui.utils.BackgroundWorker;
import com.epam.carrental.gui.view.MessageView;
import com.epam.carrental.gui.models.AbstractSwingTableModel;
import com.epam.carrental.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerController {

    @Autowired
    private BackgroundWorker inBackgroundWorker;
    @Autowired
    private MessageView messageView;

    @Autowired
    private AbstractSwingTableModel<CustomerDTO> customerTableModel;
    @Autowired
    private CustomerInputHandler customerInputHandler;
    @Autowired
    private CustomerService customerService;

    public void refreshTableView() {

        inBackgroundWorker.execute(
                customerService::readAll,
                customerTableModel::setDataAndRefreshTable,
                e -> messageView.showErrorMessage(e.getCause().getMessage()));
    }

    public void save(CustomerDTO customerDTO) {
        inBackgroundWorker.execute(
                () -> customerService.create(customerDTO),
                () -> log.info("Saved new customer "+customerDTO.getName()+" Model have to be update by JMSListener"),
                e -> messageView.showErrorMessage(e.getCause().getMessage()));
    }

    public void handleUserInput() {
        customerInputHandler.handleInput();
    }
}

