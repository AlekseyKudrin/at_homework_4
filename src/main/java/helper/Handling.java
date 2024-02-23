package helper;

import com.autodns.getway.dto.User;

import java.util.List;
import java.util.Map;

public class Handling {
    public static String buildMessage(Map<User, List<User>> data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n###################");
        for (Map.Entry<User, List<User>> element : data.entrySet()) {
            stringBuilder.append("\nПользователь:\n")
                    .append(element.getKey())
                    .append("\nимеет одинаковое имя файла аватар с:");
            for (User user : element.getValue()) {
                stringBuilder.append("\n---")
                        .append(user);
            }
        }
        return stringBuilder.toString();
    }
}
