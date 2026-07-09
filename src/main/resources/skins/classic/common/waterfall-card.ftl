<#--

    Symphony - A modern community (forum/BBS/SNS/blog) platform written in Java.
    Copyright (C) 2012-present, b3log.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

-->
<li class="waterfall-card">
    <a class="waterfall-card__media" href="${servePath}${article.articlePermalink}">
        <#if "" != article.articleThumbnailURL>
            <span style="background-image:url('${article.articleThumbnailURL}')"></span>
        <#else>
            <span class="waterfall-card__placeholder"><@icon article.articlePerfect article.articleType></@icon></span>
        </#if>
    </a>
    <div class="waterfall-card__body">
        <div class="waterfall-card__tags">
            <#list article.articleTagObjs as articleTag>
                <a rel="tag" href="${servePath}/tag/${articleTag.tagURI}">${articleTag.tagTitle}</a>
            </#list>
        </div>
        <h2>
            <a data-id="${article.oId}" data-type="${article.articleType}" rel="bookmark" href="${servePath}${article.articlePermalink}">${article.articleTitleEmoj}</a>
        </h2>
        <#if article.articlePreviewContent?? && article.articlePreviewContent != "">
            <a class="waterfall-card__abstract" href="${servePath}${article.articlePermalink}">${article.articlePreviewContent}</a>
        </#if>
        <div class="waterfall-card__footer">
            <a rel="nofollow" class="waterfall-card__author" href="${servePath}/member/${article.articleAuthorName}">
                <span class="avatar-small" style="background-image:url('${article.articleAuthorThumbnailURL48}')"></span>
                <span>${article.articleAuthorName}</span>
            </a>
            <span class="waterfall-card__stats">
                <#if article.articleCommentCount != 0>${article.articleCommentCount}${cmtLabel}</#if>
                <#if article.articleViewCount != 0><#if article.articleCommentCount != 0> · </#if><#if article.articleViewCount < 1000>${article.articleViewCount}<#else>${article.articleViewCntDisplayFormat}</#if>${viewLabel}</#if>
            </span>
        </div>
    </div>
    <#if article.articleStick gt 0>
        <span class="cb-stick tooltipped tooltipped-e" aria-label="<#if article.articleStick < 9223372036854775807>${stickLabel}${remainsLabel} ${article.articleStickRemains?c} ${minuteLabel}<#else>${adminLabel}${stickLabel}</#if>"><svg class="icon-pin"><use xlink:href="#pin"></use></svg></span>
    </#if>
</li>
