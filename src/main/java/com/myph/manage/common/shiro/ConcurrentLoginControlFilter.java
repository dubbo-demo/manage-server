package com.myph.manage.common.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Administrator
 */
public class ConcurrentLoginControlFilter extends AccessControlFilter {

    private String kickoutUrl; // 踢出后地址
    private boolean kickoutAfter = false; // 默认踢出之前登录的用户
    private int maxSession = 1; // 同一个帐号最大会话数 默认1

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;
    
    @Autowired
    private RestTemplate restTemplate;  
    
    @Value("${mycs_url}")
    private String mycsUrl;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("concurrent-login:");
    }

    /*
     * (non-Javadoc)
     * @see org.apache.shiro.web.filter.AccessControlFilter#isAccessAllowed(javax .servlet.ServletRequest,
     * javax.servlet.ServletResponse, java.lang.Object)
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.shiro.web.filter.AccessControlFilter#onAccessDenied(javax. servlet.ServletRequest,
     * javax.servlet.ServletResponse)
     */
    @SuppressWarnings("deprecation")
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            // 如果没有登录，直接进行之后的流程
            return true;
        }

        Session session = subject.getSession();
        String username = (String) subject.getPrincipal();
        Serializable sessionId = session.getId();
//        synchronized (this.cache) {
            Deque<Serializable> deque = cache.get(username);
            if (deque == null) {
                // deque = new LinkedList<Serializable>();
                deque = new ConcurrentLinkedDeque<Serializable>();
            }
            // 如果队列里没有此sessionId，且用户没有被踢出；放入队列
            if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
                deque.addLast(sessionId);
            }
            // 如果队列里的sessionId数超出最大会话数，开始踢人
            while (deque.size() > maxSession) {
                Serializable kickoutSessionId = null;
                if (!kickoutAfter) { // 如果踢出前者

                    kickoutSessionId = deque.removeFirst();
                } else { // 否则踢出后者

                    kickoutSessionId = deque.removeLast();
                }
                try {
                    Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                    if (kickoutSession != null) {
                        // 设置会话的kickout属性表示踢出了
                        kickoutSession.setAttribute("kickout", true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.cache.put(username, deque);
//        }
        // 如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null) {
            String phone = ShiroUtils.getCurrentUser().getMobilePhone();
            saveRequest(request);
            request.setAttribute("kickout", "true");
            WebUtils.issueRedirect(request, response, kickoutUrl);
            session.setTimeout(2000);
            //调用催收登录退出接口
            String url = mycsUrl + "/loginOut.htm";
            restTemplate.postForObject(url, phone, Object.class);
            return false;
        }
        return true;
    }

}
