package kitchenpos.application.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    protected MenuGroupRequest() {
    }

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
