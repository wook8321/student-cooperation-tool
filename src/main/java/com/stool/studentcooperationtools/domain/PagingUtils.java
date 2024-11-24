package com.stool.studentcooperationtools.domain;

public class PagingUtils {

    public static final int ROOM_PAGING_PARSE = 5;

    public static int getRoomPagingStartPage(final int nowPage){
        return getStartPage(nowPage, ROOM_PAGING_PARSE);
    }

    public static int getRoomPagingLastPage(final int startPage, final int totalPage){
        return getEndPage(startPage, ROOM_PAGING_PARSE);
    }

    public static int getStartPage(int currentPage,int parsePage){
        return (currentPage % parsePage == 0) ?
                (currentPage / (parsePage - 1)) * parsePage + 1 :
                (currentPage / parsePage) * parsePage + 1;
    }
    public static int getEndPage(int startPage,int totalPage){
        return Math.min((startPage - 1) + 10, totalPage);
    }
}
