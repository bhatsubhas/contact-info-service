package me.subhas.contactinfo.business.model;

import java.util.List;

public record ContactListResponse(List<ContactResponse> contacts, Integer pageNumber, Integer pageSize) {
}
