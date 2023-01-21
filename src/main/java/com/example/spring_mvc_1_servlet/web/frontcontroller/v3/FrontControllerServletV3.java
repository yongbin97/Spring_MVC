package com.example.spring_mvc_1_servlet.web.frontcontroller.v3;

import com.example.spring_mvc_1_servlet.web.frontcontroller.ModelView;
import com.example.spring_mvc_1_servlet.web.frontcontroller.MyView;
import com.example.spring_mvc_1_servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import com.example.spring_mvc_1_servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import com.example.spring_mvc_1_servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControlControllerV3", urlPatterns = "/front-cControllerV3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-cControllerV3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-cControllerV3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-cControllerV3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControlControllerV3.service");

        String requestURI = request.getRequestURI();

        ControllerV3 controller = controllerMap.get(requestURI);
        if (controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //request data -> paramMap
        Map<String, String> paramMap = createParamMap(request);

        // 논리 이름을 가지고 있는 model view
        ModelView mv = controller.process(paramMap);

        String viewName = mv.getViewName(); //논리 이름
        MyView view = viewResolver(viewName); //논리 이름 -> 물리 이름이 있는 MyView

        view.render(mv.getModel(), request, response); //model 정보 넘김
    }

    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
