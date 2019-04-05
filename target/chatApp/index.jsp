<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="view/headPage.jsp" %>

<table>
    <tr>
        <td colspan="2">
            <input type="text" id="username" placeholder="Username"/>
            <button type="button" onclick="connect();" >Connect</button>
        </td>
    </tr>
    <tr>
        <td>
            <textarea readonly="true" rows="10" cols="80" id="log"></textarea>
        </td>
    </tr>
    <tr>
        <td>
            <input type="text" size="51" id="msg" placeholder="Message"/>
            <input type="text" id="recepient" placeholder="Recepient"/>
            <button type="button" onclick="send();" >Send</button>
        </td>
    </tr>
</table>
<p>Enter "all" as value of recepient if you want the message to be broadcasted. </p>
<p>Enter username of connected person as value of recepient if you want the message to be point to point. </p>
<p>Mote that websocket connections are stateful. To change recepient, reconnect by reloading page </p>

<%@ include file="view/footerPage.html" %>
