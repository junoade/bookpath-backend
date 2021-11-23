package com.badabam.bookpath.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RootDirDTO {
    private String data;
    private String link;
    private String addDate;
    private String modifiedDate;
}
