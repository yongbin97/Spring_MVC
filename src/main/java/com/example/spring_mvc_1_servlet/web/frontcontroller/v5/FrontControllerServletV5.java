package com.example.spring_mvc_1_servlet.web.frontcontroller.v5;

import com.example.spring_mvc_1_servlet.web.frontcontroller.ModelView;
import com.example.spring_mvc_1_servlet.web.frontcontroller.MyView;
import com.example.spring_mvc_1_servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import com.example.spring_mvc_1_servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import com.example.spring_mvc_1_servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import com.example.spring_mvc_1_servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import com.example.spring_mvc_1_servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import com.example.spring_mvc_1_servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import com.example.spring_mvc_1_servlet.web.frontcontroller.v5.adapter.ControllerV3Adapter;
import com.example.spring_mvc_1_servlet.web.frontcontroller.v5.adapter.ControllerV4Adapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    // Object => 모든 handler를 넣을 수 있다.
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5(){
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerMappingMap() {
        // v3
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());
        //v4
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }
    private void initHandlerAdapters() {

        handlerAdapters.add(new ControllerV3Adapter());
        handlerAdapters.add(new ControllerV4Adapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. handler 조회
        Object handler = getHandler(request);

        if (handler == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 2. handler adapter 조회
        MyHandlerAdapter adapter = getHandlerAdapter(handler);

        // 3. handle(handler) -> 4. handler 호출 -> 5. model view 반환
        ModelView mv = adapter.handle(request, response, handler);

        // 6. viewResolver 호 -> 7. MyView 반
        String viewName = mv.getViewName(); //논리 이름
        MyView view = viewResolver(viewName); //논리 이름 -> 물리 이름이 있는 MyView

        // 8. render
        view.render(mv.getModel(), request, response); //model 정보 넘김
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)){
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        return handlerMappingMap.get(requestURI);
    }

    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

}
