package com.xtr.core.persistence.reader.account;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.account.RedReceiveBean;

public interface RedReceiveReaderMapper {

    RedReceiveBean selectOneRedReceive(RedReceiveBean redReceiveBean);

    PageList<RedReceiveBean> selectPageList(RedReceiveBean redReceiveBean, PageBounds pageBounds);
}