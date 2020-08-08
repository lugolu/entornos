package ar.com.thinksoft.resources;

import java.util.HashMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

@ManagedBean(name="msg")
@RequestScoped
@SuppressWarnings({ "serial", "rawtypes" })
public class MessageProvider extends HashMap {

	@ManagedProperty(value="#{msgMgr}")
	private MessageManager msgMgr;
    public MessageProvider() {}

    @Override
    public Object get(Object key) {
        return msgMgr.getMessage((String)key);
    }

    public void setMsgMgr(MessageManager msgMgr) {
        this.msgMgr = msgMgr;
    }

    public MessageManager getMsgMgr() {
        return msgMgr;
    }

}
