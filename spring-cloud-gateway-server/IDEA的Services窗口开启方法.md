打开项目的**.idea**文件（`Maven父子工程中父工程`）中的**workspace.xml**文件，在**component**的同级标签的文件的末尾添加如下数据：

```xml
<component name="RunDashboard">
    <option name="configurationTypes">
      <set>
        <option value="SpringBootApplicationConfigurationType" />
      </set>
    </option>
    <option name="ruleStates">
      <list>
        <RuleState>
          <option name="name" value="ConfigurationTypeDashboardGroupingRule" />
        </RuleState>
        <RuleState>
          <option name="name" value="StatusDashboardGroupingRule" />
        </RuleState>
      </list>
    </option>
</component>
```

添加完成后，就可以看到在IDEA的底部出现**Services**窗口了（若添加后Services窗口并未出现，请重启IDEA）

![image-20210323222904702](C:\Users\22341\AppData\Roaming\Typora\typora-user-images\image-20210323222904702.png)

在项目上进行点击鼠标右键，可以进行启动项目，各个服务启动后效果如下，个人认为这种形式进行管理服务更方便，更清楚！

![image-20210323223302346](C:\Users\22341\AppData\Roaming\Typora\typora-user-images\image-20210323223302346.png)