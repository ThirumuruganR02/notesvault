import {
  createContext,
  useCallback,
  useContext,
  useMemo,
  useState,
  type ReactNode,
} from "react";
import * as api from "../api/client";
import type { AuthResponse } from "../types";

type UserBrief = Pick<AuthResponse, "id" | "username" | "email">;

type AuthContextValue = {
  user: UserBrief | null;
  isAuthenticated: boolean;
  login: (email: string, password: string) => Promise<void>;
  register: (username: string, email: string, password: string) => Promise<void>;
  logout: () => void;
};

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<UserBrief | null>(() => {
    const t = api.getToken();
    const u = api.getStoredUser();
    return t && u ? u : null;
  });

  const login = useCallback(async (email: string, password: string) => {
    const auth = await api.login({ email, password });
    api.persistSession(auth);
    setUser({ id: auth.id, username: auth.username, email: auth.email });
  }, []);

  const register = useCallback(async (username: string, email: string, password: string) => {
    const auth = await api.register({ username, email, password });
    api.persistSession(auth);
    setUser({ id: auth.id, username: auth.username, email: auth.email });
  }, []);

  const logout = useCallback(() => {
    api.clearSession();
    setUser(null);
  }, []);

  const value = useMemo(
    () => ({
      user,
      isAuthenticated: !!user,
      login,
      register,
      logout,
    }),
    [user, login, register, logout],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider");
  return ctx;
}
