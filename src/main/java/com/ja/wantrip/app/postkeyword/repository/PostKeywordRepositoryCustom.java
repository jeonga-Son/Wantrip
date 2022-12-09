package com.ja.wantrip.app.postkeyword.repository;

import com.ja.wantrip.app.postkeyword.entity.PostKeyword;

import java.util.List;

public interface PostKeywordRepositoryCustom {
    List<PostKeyword> getQslAllByAuthorId(Long authorId);
}
