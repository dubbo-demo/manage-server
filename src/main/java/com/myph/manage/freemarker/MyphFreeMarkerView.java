package com.myph.manage.freemarker;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 扩展spring的FreemarkerView，加上base属性。
 * <p/>
 * 支持jsp标签，Application、Session、Request、RequestParameters属性
 */
public class MyphFreeMarkerView extends FreeMarkerView {
    public static final String VERSION_NO_VALUE = String.valueOf(System.currentTimeMillis());

    /**
     * 在model中增加部署路径base，方便处理部署路径问题。
     */
    @SuppressWarnings("unchecked")
    protected void exposeHelpers(Map model, HttpServletRequest request)
            throws Exception {
        super.exposeHelpers(model, request);
        //当前项目项目根目录
        UrlPathHelper helper = new UrlPathHelper();
        //String uri = helper.getOriginatingRequestUri(request);
        String ctxPath = helper.getOriginatingContextPath(request);
        int port = request.getServerPort();
        String pathStr = request.getScheme() + "://" + request.getServerName();
        if (80 != port) {
            pathStr = pathStr + ":" + port;
        }
        pathStr += request.getContextPath();

        model.put(FreeMakerConstants.SERVER_CONTEXT_PATH, request.getContextPath());
        model.put(FreeMakerConstants.WEBCDN_CONTEXT_PATH, pathStr);
        model.put(FreeMakerConstants.WEB_CONTEXT_VERSION, VERSION_NO_VALUE);

    }
}