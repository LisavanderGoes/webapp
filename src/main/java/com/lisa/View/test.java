package com.lisa.View;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;

@Route("test")
public class test extends VerticalLayout {

    private Grid<Data> grid = new Grid<>(Data.class);

    public test() {
        //list.add(new Data("name2", 2));
        grid.setColumns("Name", "Number");
        updateList();
        add(grid);
        setSizeFull();
    }

    private void updateList() {
       // grid.setItems(list);
    }
}
