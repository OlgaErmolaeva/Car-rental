package com.epam.carrental.models;

import lombok.Getter;
import lombok.Setter;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractSwingTableModel<DTO> extends AbstractTableModel {

    @Getter
    transient String tableName;
    @Setter
    transient List<DTO> data = new ArrayList<>();

    transient Map<String,Function<DTO, Object>> columnAndActionMap;


    public AbstractSwingTableModel() {
        this.columnAndActionMap=new LinkedHashMap<>();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnAndActionMap.keySet().stream().skip(columnIndex).findFirst().orElse("");
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnAndActionMap.keySet().size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Function<DTO, Object> function=this.columnAndActionMap.values().stream().skip(columnIndex).findFirst().get();
        return function.apply(data.get(rowIndex));
    }

    public DTO getModel(int rowIndex) {
        return data.get(rowIndex);
    }

}
